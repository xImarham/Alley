package dev.revere.alley.base.queue.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.QueueProfile;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class QueueRunnable implements Runnable {
    protected final Alley plugin = Alley.getInstance();

    @Override
    public void run() {
        this.plugin.getQueueService().getQueues().forEach(this::processQueue);
    }

    /**
     * Method to process the queue.
     *
     * @param queue The queue.
     */
    public void processQueue(Queue queue) {
        Iterator<QueueProfile> profileIterator = queue.getProfiles().iterator();
        while (profileIterator.hasNext()) {
            QueueProfile profile = profileIterator.next();
            Player player = Bukkit.getPlayer(profile.getUuid());
            if (player == null || !player.isOnline() || !this.plugin.getProfileService().getProfile(profile.getUuid()).getState().equals(EnumProfileState.WAITING)) {
                queue.removePlayer(profile);
                profileIterator.remove();
                if (player != null) {
                    player.sendMessage(CC.translate("&cYou have been removed from the queue due to being offline or state change."));
                }
                continue;
            }

            if (profile.getElapsedTime() >= queue.getMaxQueueTime()) {
                queue.removePlayer(profile);
                profileIterator.remove();
                player.sendMessage(CC.translate("&cYou have been removed from the queue due to inactivity"));
            } else {
                profile.queueRange(player);
            }
        }

        if (queue.isDuos()) {
            processDuosQueue(queue);
        } else {
            processSoloQueue(queue);
        }
    }

    private void processSoloQueue(Queue queue) {
        if (queue.getProfiles().size() < 2) {
            return;
        }

        List<QueueProfile> availableSoloPlayers = queue.getProfiles().stream()
                .filter(queueProfile -> this.plugin.getPartyService().getParty(Bukkit.getPlayer(queueProfile.getUuid())) == null)
                .sorted(Comparator.comparingLong(QueueProfile::getElapsedTime))
                .collect(Collectors.toList());

        if (availableSoloPlayers.size() < 2) {
            return;
        }

        for (int i = 0; i < availableSoloPlayers.size(); i++) {
            QueueProfile firstProfile = availableSoloPlayers.get(i);
            Player firstPlayer = Bukkit.getPlayer(firstProfile.getUuid());

            if (firstPlayer == null || !firstPlayer.isOnline() || !this.plugin.getProfileService().getProfile(firstProfile.getUuid()).getState().equals(EnumProfileState.WAITING)) {
                queue.removePlayer(firstProfile);
                continue;
            }

            for (int j = i + 1; j < availableSoloPlayers.size(); j++) {
                QueueProfile secondProfile = availableSoloPlayers.get(j);
                Player secondPlayer = Bukkit.getPlayer(secondProfile.getUuid());
                if (secondPlayer == null || !secondPlayer.isOnline() || !this.plugin.getProfileService().getProfile(secondProfile.getUuid()).getState().equals(EnumProfileState.WAITING)) {
                    queue.removePlayer(secondProfile);
                    continue;
                }

                GamePlayerList gamePlayerList = getGamePlayerList(firstPlayer, secondPlayer, firstProfile, secondProfile);
                GameParticipantList gameParticipantList = getSoloGameParticipantList(gamePlayerList);

                AbstractArena arena = this.getArena(queue);
                if (arena == null || arena.getType().equals(EnumArenaType.FFA)) {
                    firstPlayer.sendMessage(CC.translate("&cThere are no available arenas for this kit"));
                    secondPlayer.sendMessage(CC.translate("&cThere are no available arenas for this kit"));
                    queue.removePlayer(firstProfile);
                    queue.removePlayer(secondProfile);
                    return;
                }

                this.plugin.getMatchService().createAndStartMatch(
                        queue.getKit(), arena, gameParticipantList.participantA, gameParticipantList.participantB, false, true, queue.isRanked()
                );

                this.clearQueueProfiles(queue, Arrays.asList(firstProfile.getUuid(), secondProfile.getUuid()), false);
                return;
            }
        }
    }

    private void processDuosQueue(Queue queue) {
        if (queue.getProfiles().size() < 2 && queue.getTotalPlayerCount() < 4) {
            return;
        }

        List<QueueProfile> availableProfiles = new ArrayList<>(queue.getProfiles());
        availableProfiles.sort(Comparator.comparingLong(QueueProfile::getElapsedTime));

        List<QueueProfile> fullParties = availableProfiles.stream()
                .filter(qp -> {
                    Player leader = Bukkit.getPlayer(qp.getUuid());
                    if (leader == null) return false;
                    Party party = plugin.getPartyService().getPartyByLeader(leader);
                    return party != null && party.getMembers().size() == 2;
                })
                .collect(Collectors.toList());

        List<QueueProfile> soloDuosPlayers = availableProfiles.stream()
                .filter(qp -> {
                    Player player = Bukkit.getPlayer(qp.getUuid());
                    if (player == null) return false;
                    Party party = plugin.getPartyService().getPartyByLeader(player);
                    return party == null || party.getMembers().size() == 1;
                })
                .collect(Collectors.toList());

        // Strategy:
        // 1. Try to match two full parties (2v2)
        // 2. Try to match one full party with two solo-duo players
        // 3. Try to match four solo-duo players

        // 1. Try to match two full parties
        if (fullParties.size() >= 2) {
            QueueProfile team1LeaderProfile = fullParties.get(0);
            QueueProfile team2LeaderProfile = fullParties.get(1);

            if (tryMatchDuos(queue, team1LeaderProfile, team2LeaderProfile)) {
                return;
            }
        }

        // 2. Try to match one full party with two solo-duo players
        if (!fullParties.isEmpty() && soloDuosPlayers.size() >= 2) {
            QueueProfile partyLeaderProfile = fullParties.get(0);
            QueueProfile soloDuo1Profile = soloDuosPlayers.get(0);
            QueueProfile soloDuo2Profile = soloDuosPlayers.get(1);

            if (tryMatchDuos(queue, partyLeaderProfile, soloDuo1Profile, soloDuo2Profile)) {
                return;
            }
        }

        // 3. Try to match four solo-duo players
        if (soloDuosPlayers.size() >= 4) {
            QueueProfile soloDuo1Profile = soloDuosPlayers.get(0);
            QueueProfile soloDuo2Profile = soloDuosPlayers.get(1);
            QueueProfile soloDuo3Profile = soloDuosPlayers.get(2);
            QueueProfile soloDuo4Profile = soloDuosPlayers.get(3);

            tryMatchDuos(queue, soloDuo1Profile, soloDuo2Profile, soloDuo3Profile, soloDuo4Profile);
        }
    }

    private boolean tryMatchDuos(Queue queue, QueueProfile... potentialPlayers) {
        if (potentialPlayers.length < 2 || potentialPlayers.length > 4) {
            Logger.logError("Invalid number of potential players for tryMatchDuos: " + potentialPlayers.length);
            return false;
        }

        List<Player> onlinePlayers = new ArrayList<>();
        List<QueueProfile> validQueueProfiles = new ArrayList<>();
        for (QueueProfile qp : potentialPlayers) {
            Player p = Bukkit.getPlayer(qp.getUuid());
            if (p == null || !p.isOnline() || !plugin.getProfileService().getProfile(p.getUniqueId()).getState().equals(EnumProfileState.WAITING)) {
                Logger.log("One of the potential players is not ready: " + qp.getUuid());
                return false;
            }
            onlinePlayers.add(p);
            validQueueProfiles.add(qp);
        }

        List<Player> allMatchPlayers = new ArrayList<>();

        Player team1Leader = onlinePlayers.get(0);
        QueueProfile team1LeaderProfile = validQueueProfiles.get(0);
        allMatchPlayers.add(team1Leader);

        Party team1Party = this.plugin.getPartyService().getPartyByLeader(team1Leader);
        if (team1Party != null && team1Party.getMembers().size() == 2) {
            team1Party.getMembers().stream()
                    .filter(uuid -> !uuid.equals(team1Leader.getUniqueId()))
                    .findFirst()
                    .ifPresent(memberUUID -> {
                        Player memberPlayer = Bukkit.getPlayer(memberUUID);
                        if (memberPlayer != null && memberPlayer.isOnline()) {
                            allMatchPlayers.add(memberPlayer);
                        }
                    });
        }

        int team2StartIndex;
        if (potentialPlayers.length == 2) {
            team2StartIndex = 1;
        } else if (potentialPlayers.length == 3) {
            team2StartIndex = 1;
        } else {
            team2StartIndex = 2;
        }

        Player team2Leader = onlinePlayers.get(team2StartIndex);
        QueueProfile team2LeaderProfile = validQueueProfiles.get(0);
        allMatchPlayers.add(team2Leader);

        Party team2Party = this.plugin.getPartyService().getPartyByLeader(team2Leader);
        if (team2Party != null && team2Party.getMembers().size() == 2) {
            team2Party.getMembers().stream()
                    .filter(uuid -> !uuid.equals(team2Leader.getUniqueId()))
                    .findFirst()
                    .ifPresent(memberUUID -> {
                        Player memberPlayer = Bukkit.getPlayer(memberUUID);
                        if (memberPlayer != null && memberPlayer.isOnline()) {
                            allMatchPlayers.add(memberPlayer);
                        }
                    });
        }

        for (int i = 1; i < validQueueProfiles.size(); i++) {
           if (i == team2StartIndex) continue;

           Player soloPlayer = onlinePlayers.get(i);

           if (!allMatchPlayers.contains(soloPlayer)) {
               allMatchPlayers.add(soloPlayer);
           }
        }

        if (allMatchPlayers.size() != 4) {
            Logger.log("Expected exactly 4 players for duos match, but got: " + allMatchPlayers.size());
            return false;
        }

        MatchGamePlayerImpl gameLeaderA = new MatchGamePlayerImpl(team1Leader.getUniqueId(), team1Leader.getName(), team1LeaderProfile.getElo());
        MatchGamePlayerImpl gameLeaderB = new MatchGamePlayerImpl(team2Leader.getUniqueId(), team2Leader.getName(), team2LeaderProfile.getElo());

        GameParticipant<MatchGamePlayerImpl> participantA = new TeamGameParticipant<>(gameLeaderA);
        GameParticipant<MatchGamePlayerImpl> participantB = new TeamGameParticipant<>(gameLeaderB);

        for (Player player : allMatchPlayers) {
            if (player.equals(team1Leader) || player.equals(team2Leader)) {
                continue;
            }

            int playerElo = 1000;
            for (QueueProfile queueProfile : validQueueProfiles) {
                if (queueProfile.getUuid().equals(player.getUniqueId())) {
                    playerElo = queueProfile.getElo();
                    break;
                }
            }

            MatchGamePlayerImpl gamePlayer = new MatchGamePlayerImpl(player.getUniqueId(), player.getName(), playerElo);

            if (team1Party != null && team1Party.getMembers().contains(player.getUniqueId())) {
                participantA.addPlayer(gamePlayer);
            } else if (team2Party != null && team2Party.getMembers().contains(player.getUniqueId())) {
                participantB.addPlayer(gamePlayer);
            } else {
                if (participantA.getPlayerSize() < participantB.getPlayerSize()) {
                    participantA.addPlayer(gamePlayer);
                } else {
                    participantB.addPlayer(gamePlayer);
                }
            }
        }

        if (participantA.getPlayerSize() != 2 || participantB.getPlayerSize() != 2) {
            Logger.log("Teams don't have exactly 2 players each. Team A: " + participantA.getPlayerSize() + " Team B: " + participantB.getPlayerSize());
            return false;
        }

        AbstractArena arena = this.getArena(queue);
        if (arena == null || arena.getType().equals(EnumArenaType.FFA)) {
            allMatchPlayers.forEach(p -> p.sendMessage(CC.translate("&cThere are no available arenas for the kit you are queuing.")));

            List<UUID> allUUIDsToRemove = allMatchPlayers.stream()
                    .map(Player::getUniqueId)
                    .collect(Collectors.toList());

            clearQueueProfiles(queue, allUUIDsToRemove, true);
        }

        this.plugin.getMatchService().createAndStartMatch(
                queue.getKit(), arena, participantA, participantB, true, false, queue.isRanked()
        );

        List<UUID> allUUIDsToRemove = allMatchPlayers.stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toList());
        clearQueueProfiles(queue, allUUIDsToRemove, false);
        return true;
    }

    /**
     * Method to get an arena.
     *
     * @param queue The queue.
     * @return The arena.
     */
    private AbstractArena getArena(Queue queue) {
        return this.plugin.getArenaService().getRandomArena(queue.getKit());
    }

    /**
     * Method to clear queue profiles for multiple players after a match has been created.
     * This method is responsible for all cleanup, including updating player profiles and
     * removing the entry from the queue list. It does NOT call queue.removePlayer().
     *
     * @param queue       The queue.
     * @param playerUUIDs The UUIDs of all players who were placed in the match.
     */
    public void clearQueueProfiles(Queue queue, List<UUID> playerUUIDs, boolean removeQueue /* maybe smth like this */) {
        Set<QueueProfile> uniqueProfiles = new HashSet<>();
        for (UUID uuid : playerUUIDs) {
            Profile profile = this.plugin.getProfileService().getProfile(uuid);
            if (profile != null && profile.getQueueProfile() != null) {
                uniqueProfiles.add(profile.getQueueProfile());
            }
        }

        for (QueueProfile queueProfile : uniqueProfiles) {
            if (removeQueue) {
                queue.removePlayer(queueProfile);
            } else {
                Player leader = Bukkit.getPlayer(queueProfile.getUuid());
                if (leader == null) continue;

                Party party = plugin.getPartyService().getParty(leader);

                List<UUID> membersToClean = new ArrayList<>();
                if (queue.isDuos() && party != null) {
                    membersToClean.addAll(party.getMembers());
                } else {
                    membersToClean.add(leader.getUniqueId());
                }

                for (UUID memberId : membersToClean) {
                    Profile memberProfile = this.plugin.getProfileService().getProfile(memberId);
                    if (memberProfile != null) {
                        memberProfile.setQueueProfile(null);
                    }
                }

                queue.getProfiles().remove(queueProfile);
            }
        }
    }

    /**
     * Method to get the game participant list.
     *
     * @param gamePlayerList The game player list.
     * @return The game participant list.
     */
    private @NotNull GameParticipantList getSoloGameParticipantList(GamePlayerList gamePlayerList) {
        return new GameParticipantList(
                new GameParticipant<>(gamePlayerList.getFirstMatchGamePlayer()),
                new GameParticipant<>(gamePlayerList.getSecondMatchGamePlayer())
        );
    }

    /**
     * Method to get the game player list.
     *
     * @param firstPlayer  The first player.
     * @param secondPlayer The second player.
     * @return The game player list.
     */
    private @NotNull GamePlayerList getGamePlayerList(Player firstPlayer, Player secondPlayer, QueueProfile firstProfile, QueueProfile secondProfile) {
        return new GamePlayerList(
                new MatchGamePlayerImpl(firstPlayer.getUniqueId(), firstPlayer.getName(), firstProfile.getElo()),
                new MatchGamePlayerImpl(secondPlayer.getUniqueId(), secondPlayer.getName(), secondProfile.getElo())
        );
    }

    @Getter
    private static class GameParticipantList {
        public final GameParticipant<MatchGamePlayerImpl> participantA;
        public final GameParticipant<MatchGamePlayerImpl> participantB;

        /**
         * Constructor for the GameParticipantList class.
         *
         * @param participantA The first participant.
         * @param participantB The second participant.
         */
        public GameParticipantList(GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
            this.participantA = participantA;
            this.participantB = participantB;
        }

        public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
            return Arrays.asList(this.participantA, this.participantB);
        }
    }

    @Getter
    private static class GamePlayerList {
        public final MatchGamePlayerImpl firstMatchGamePlayer;
        public final MatchGamePlayerImpl secondMatchGamePlayer;

        /**
         * Constructor for the GamePlayerList class.
         *
         * @param firstMatchGamePlayer  The first match game player.
         * @param secondMatchGamePlayer The second match game player.
         */
        public GamePlayerList(MatchGamePlayerImpl firstMatchGamePlayer, MatchGamePlayerImpl secondMatchGamePlayer) {
            this.firstMatchGamePlayer = firstMatchGamePlayer;
            this.secondMatchGamePlayer = secondMatchGamePlayer;
        }
    }
}