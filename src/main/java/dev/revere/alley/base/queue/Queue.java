package dev.revere.alley.base.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class Queue {
    protected final Alley plugin = Alley.getInstance();

    private final LinkedList<QueueProfile> profiles;
    private final Kit kit;
    private final boolean ranked;
    private final long maxQueueTime;
    private final boolean duos;

    /**
     * Constructor for the Queue class.
     *
     * @param kit The kit associated with the queue.
     */
    public Queue(Kit kit, boolean ranked, boolean duos) {
        this.profiles = new LinkedList<>();
        this.kit = kit;
        this.ranked = ranked;
        this.duos = duos;
        this.maxQueueTime = 300000L; // 5 minutes
        this.plugin.getQueueService().getQueues().add(this);
    }

    /**
     * Gets the amount of people playing that queue.
     *
     * @return The amount of people playing that queue.
     */
    public int getQueueFightCount() {
        return this.plugin.getMatchService().getMatches().stream().filter(match -> match.getQueue().equals(this)).toArray().length;
    }

    /**
     * Gets the queue type.
     *
     * @return The queue type.
     */
    public String getQueueType() {
        return (this.ranked ? "Ranked" : "Unranked") + (this.duos ? " Duos" : " Solo");
    }

    /**
     * Adds a player to the queue.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player, int elo) {
        UUID uuid = player.getUniqueId();

        Profile profile = this.plugin.getProfileService().getProfile(uuid);
        Party party = this.plugin.getPartyService().getParty(player);

        if (this.isDuos() && party != null) {
            for (UUID memberId : party.getMembers()) {
                Profile memberProfile = this.plugin.getProfileService().getProfile(memberId);
                if (memberProfile != null && memberProfile.getQueueProfile() != null) {
                    player.sendMessage(CC.translate("&cSomeone in your party is already in a queue."));
                    return;
                }
            }
        } else if (this.profiles.stream().anyMatch(queueProfile -> queueProfile.getUuid().equals(uuid))) {
            player.sendMessage(CC.translate("&cYou're already in a queue."));
            return;
        }

        if (!this.isDuos() && party != null) {
            player.sendMessage(CC.translate("&cYou cannot queue for 1v1 while in a party."));
            return;
        }


        if (this.isDuos()) {
            if (party != null && !party.getLeader().equals(player)) {
                player.sendMessage(CC.translate("&cOnly the party leader can queue."));
                return;
            }

            if (party != null && party.getMembers().size() > 2) {
                player.sendMessage(CC.translate("&cYour party size is too large for duo queues."));
                return;
            }

            if (party != null && party.getMembers().size() == 2) {
                for (UUID memberId : party.getMembers()) {
                    if (Bukkit.getPlayer(memberId) == null || !Bukkit.getPlayer(memberId).isOnline()) {
                        player.sendMessage(CC.translate("&cAll party members must be online to queue."));
                        return;
                    }

                    Profile memberProfile = this.plugin.getProfileService().getProfile(memberId);
                    if (memberProfile.getState() != EnumProfileState.LOBBY) {
                        player.sendMessage(CC.translate("&cAll party members must be in the lobby to queue."));
                        return;
                    }
                }
            } else {
                if (profile.getState() != EnumProfileState.LOBBY) {
                    player.sendMessage(CC.translate("&cYou must be in the lobby to queue."));
                    return;
                }

                if (party == null || party.getMembers().size() == 1) {
                    player.sendMessage(CC.translate("&eYou are queuing for duos solo. A random teammate will be selected."));
                }
            }
        } else {
            if (profile.getState() != EnumProfileState.LOBBY) {
                player.sendMessage(CC.translate("&cYou must be in the lobby to queue."));
                return;
            }
        }

        QueueProfile queueProfile = new QueueProfile(this, uuid);
        queueProfile.setElo(elo);

        profile.setQueueProfile(queueProfile);
        profile.setState(EnumProfileState.WAITING);

        this.profiles.add(queueProfile);

        if (this.isDuos() && party != null && party.getMembers().size() > 1) {
            for (UUID memberId : party.getMembers()) {
                if (!memberId.equals(uuid)) {
                    {
                        Profile memberProfile = this.plugin.getProfileService().getProfile(memberId);
                        if (memberProfile != null) {
                            memberProfile.setQueueProfile(queueProfile);
                            memberProfile.setState(EnumProfileState.WAITING);
                            this.plugin.getHotbarService().applyHotbarItems(player);
                            Player memberPlayer = Bukkit.getPlayer(memberId);
                            if (memberPlayer != null) {
                                memberPlayer.sendMessage(CC.translate("&fYour party leader has joined the &6" + queueProfile.getQueue().getKit().getDisplayName() + " &fqueue."));
                            }
                        }
                    }
                }
            }
        }

        player.sendMessage(CC.translate("&aYou've joined the &6" + queueProfile.getQueue().getKit().getName() + " &aqueue."));

        this.plugin.getHotbarService().applyHotbarItems(player);
    }

    /**
     * Removes a player from the queue.
     *
     * @param queueProfile The queue profile to remove.
     */
    public void removePlayer(QueueProfile queueProfile) {
        UUID playerToRemoveUUID = queueProfile.getUuid();
        Profile playerToRemoveProfile = this.plugin.getProfileService().getProfile(playerToRemoveUUID);
        Player playerToRemove = Bukkit.getPlayer(playerToRemoveUUID);

        Party party = this.plugin.getPartyService().getParty(playerToRemove);
        if (this.isDuos() && party != null && party.getLeader().getUniqueId().equals(playerToRemove.getUniqueId())) {
            for (UUID memberId : party.getMembers()) {
                Profile memberProfile = this.plugin.getProfileService().getProfile(memberId);
                if (memberProfile != null && memberProfile.getQueueProfile() != null) {
                    memberProfile.setQueueProfile(null);
                    memberProfile.setState(EnumProfileState.LOBBY);
                    Player memberPlayer = Bukkit.getPlayer(memberId);
                    if (memberPlayer != null) {
                        this.plugin.getHotbarService().applyHotbarItems(memberPlayer);
                        memberPlayer.sendMessage(CC.translate("&cYour party has left the queue."));
                    }
                }
            }
            this.profiles.remove(queueProfile);
        } else {
            this.profiles.remove(queueProfile);

            if (playerToRemoveProfile != null) {
                playerToRemoveProfile.setQueueProfile(null);
                playerToRemoveProfile.setState(EnumProfileState.LOBBY);
            }

            if (playerToRemove != null) {
                this.plugin.getHotbarService().applyHotbarItems(playerToRemove);
                playerToRemove.sendMessage(CC.translate("&cYou've left the queue."));
            }
        }
    }

    /**
     * Gets the profile of a player.
     *
     * @param uuid The UUID of the player.
     * @return The profile object.
     */
    public Profile getProfile(UUID uuid) {
        return this.plugin.getProfileService().getProfile(uuid);
    }

    public int getTotalPlayerCount() {
        int count = 0;
        for (QueueProfile queueProfile : this.profiles) {
            Player leader = Bukkit.getPlayer(queueProfile.getUuid());
            if (leader != null) {
                Party party = this.plugin.getPartyService().getParty(leader);
                if (party != null && party.getMembers().size() > 1) {
                    count += party.getMembers().size();
                } else {
                    count += 1;
                }
            } else {
                count += 1;
            }
        }
        return count;
    }
}