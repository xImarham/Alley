package dev.revere.alley.feature.queue.runnable;

import dev.revere.alley.feature.kit.settings.impl.KitSettingBattleRushImpl;
import dev.revere.alley.game.match.impl.MatchRoundsRegularImpl;
import lombok.Getter;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.feature.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.impl.MatchLivesRegularImpl;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueProfile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class QueueRunnable implements Runnable {
    @Override
    public void run() {
        Alley.getInstance().getQueueRepository().getQueues()
                .forEach(this::processQueue);
    }

    /**
     * Method to process the queue.
     *
     * @param queue The queue.
     */
    public void processQueue(Queue queue) {
        queue.getProfiles().forEach(QueueProfile::queueRange);
        queue.getProfiles().forEach(profile -> {
            if (profile.getElapsedTime() >= 300000) {
                if (queue.getProfile(profile.getUuid()).getState().equals(EnumProfileState.WAITING)) {
                    queue.getProfiles().remove(profile);
                    queue.removePlayer(profile);
                    Player player = Alley.getInstance().getServer().getPlayer(profile.getUuid());
                    player.sendMessage(CC.translate("&cYou have been removed from the queue due to inactivity."));
                    Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
                } else {
                    Logger.logError("&cPlayer &4" + Bukkit.getPlayer(profile.getUuid()) + "&c couldn't be removed from the queue because their state was changed.");
                }
            }
        });

        if (queue.getProfiles().size() < 2) {
            return;
        }

        for (QueueProfile firstProfile : queue.getProfiles()) {
            Optional<Player> firstPlayerOpt = getPlayer(firstProfile);
            if (!firstPlayerOpt.isPresent()) {
                return;
            }

            for (QueueProfile secondProfile : queue.getProfiles()) {
                if (!firstProfile.equals(secondProfile)) {
                    Optional<Player> secondPlayerOpt = getPlayer(secondProfile);
                    if (!secondPlayerOpt.isPresent()) {
                        return;
                    }

                    this.clearQueueProfiles(queue, firstProfile, secondProfile);
                    GamePlayerList gamePlayerList = getGamePlayerList(firstPlayerOpt.get(), secondPlayerOpt.get(), firstProfile, secondProfile);
                    GameParticipantList gameParticipantList = getGameParticipantList(gamePlayerList);
                    this.processGame(queue, gameParticipantList);
                }
            }
        }
    }

    /**
     * Method to get a player.
     *
     * @param profile The queue profile.
     * @return The player.
     */
    private Optional<Player> getPlayer(QueueProfile profile) {
        return Optional.ofNullable(Alley.getInstance().getServer().getPlayer(profile.getUuid()));
    }

    /**
     * Method to process the game.
     *
     * @param queue               The queue.
     * @param gameParticipantList The game participant list.
     */
    private void processGame(Queue queue, GameParticipantList gameParticipantList) {
        Arena arena = getArena(queue);

        if (arena == null || arena.getType().equals(EnumArenaType.FFA)) {
            gameParticipantList.getParticipants().forEach(participant -> {
                Player player = Alley.getInstance().getServer().getPlayer(participant.getPlayer().getUuid());
                player.sendMessage(CC.translate("&cThere are no available arenas for the kit you're playing."));
            });
            return;
        }

        AbstractMatch match = getMatchType(queue, gameParticipantList, arena);
        match.startMatch();
    }

    /**
     * Method to get the match type.
     *
     * @param queue               The queue.
     * @param gameParticipantList The game participant list.
     * @param arena               The arena.
     * @return The match type.
     */
    private @NotNull AbstractMatch getMatchType(Queue queue, GameParticipantList gameParticipantList, Arena arena) {
        if (queue.getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
            return new MatchLivesRegularImpl(queue, queue.getKit(), arena, queue.isRanked(), gameParticipantList.getParticipantA(), gameParticipantList.getParticipantB());
        } else if (queue.getKit().isSettingEnabled(KitSettingBattleRushImpl.class)) {
            return new MatchRoundsRegularImpl(queue, queue.getKit(), arena, queue.isRanked(), gameParticipantList.getParticipantA(), gameParticipantList.getParticipantB(), 3);
        } else {
            return new MatchRegularImpl(queue, queue.getKit(), arena, queue.isRanked(), gameParticipantList.getParticipantA(), gameParticipantList.getParticipantB());
        }
    }

    /**
     * Method to get an arena.
     *
     * @param queue The queue.
     * @return The arena.
     */
    private Arena getArena(Queue queue) {
        return Alley.getInstance().getArenaRepository().getRandomArena(queue.getKit());
    }

    /**
     * Method to clear the queue profiles.
     *
     * @param queue         The queue.
     * @param firstProfile  The first profile.
     * @param secondProfile The second profile.
     */
    private void clearQueueProfiles(Queue queue, QueueProfile firstProfile, QueueProfile secondProfile) {
        queue.getProfiles().remove(firstProfile);
        queue.getProfiles().remove(secondProfile);
    }

    /**
     * Method to get the game participant list.
     *
     * @param gamePlayerList The game player list.
     * @return The game participant list.
     */
    private @NotNull GameParticipantList getGameParticipantList(GamePlayerList gamePlayerList) {
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
            return Arrays.asList(getParticipantA(), getParticipantB());
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