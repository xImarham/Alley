package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 21:42
 */
@Getter
@Setter
public class Party {
    private Player leader;
    private boolean shared;
    private List<UUID> members;
    private String chatFormat = Alley.getInstance().getConfigHandler().getConfig("messages.yml").getString("party.chat-format");

    /**
     * Constructor for the Party class.
     *
     * @param leader The leader of the party.
     */
    public Party(Player leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.addMember(leader);
        this.registerParty();
    }

    /**
     * Disbands the party.
     */
    public void disbandParty() {
        this.notifyPartyExcludeLeader("&cThe party has been disbanded.");
        this.unregisterParty();
        this.resetAllPlayers();
    }

    /**
     * Joins a player to the party.
     *
     * @param player The player to join.
     */
    public void joinParty(Player player) {
       if (!this.isMember(player)) {
           this.addMember(player);
           this.notifyParty("&a" + player.getName() + " has joined the party.");
           this.setupProfile(player, true);
        }
    }

    /**
     * Removes a member from the party.
     *
     * @param player The member that leaves.
     */
    public void leaveParty(Player player) {
        if (this.isMember(player)) {
            this.removeMember(player);
            this.notifyParty("&c" + player.getName() + " has left the party.");
            this.setupProfile(player, false);
        }
    }

    public void kickPlayer(Player player) {
        if (this.isMember(player)) {
            this.removeMember(player);
            this.notifyParty("&4" + player.getName() + " &cwas kicked from the party.");
            this.setupProfile(player, false);
        }
    }

    /**
     * Checks if a player is in the party.
     *
     * @param player The player to check.
     * @return Whether the player is in the party.
     */
    private boolean isMember(Player player) {
        return this.members.contains(player.getUniqueId());
    }

    /**
     * Kicks a member from the party.
     *
     * @param player The member to kick.
     */
    private void removeMember(Player player) {
        this.members.remove(player.getUniqueId());
    }

    /**
     * Adds a member to the party.
     *
     * @param player The member to add.
     */
    private void addMember(Player player) {
        this.members.add(player.getUniqueId());
    }

    /**
     * Gets the players in the party.
     *
     * @return The players in the party.
     */
    public List<Player> getPlayersInParty() {
        List<Player> players = new ArrayList<>();
        for (UUID member : this.members) {
            Player player = Alley.getInstance().getServer().getPlayer(member);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    /**
     * Sets up the profile of a player.
     *
     * @param player The player to set up the profile of.
     * @param join   Whether the player is joining the party.
     */
    private void setupProfile(Player player, boolean join) {
        Profile profile = getProfile(player);
        profile.setParty(join ? this : null);
        if (join && (profile.getState() == EnumProfileState.LOBBY || profile.getState() == EnumProfileState.WAITING)) {
            Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.PARTY);
        } else {
            Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        }
    }

    private void resetAllPlayers() {
        this.getPlayersInParty().forEach(this::resetPlayer);
    }

    /**
     * Resets the player's profile.
     *
     * @param player The player to reset.
     */
    private void resetPlayer(Player player) {
        this.setupProfile(player, false);
    }

    /**
     * Forces a message to be sent to the party regardless of their profile settings.
     *
     * @param message The message to send.
     */
    public void notifyPartyForcefully(String message) {
        this.getPlayersInParty().forEach(player -> player.sendMessage(CC.translate(message)));
    }

    /**
     * Notifies the party members based on their profile settings.
     *
     * @param message The message to send.
     */
    public void notifyParty(String message) {
        this.getPlayersInParty().forEach(player -> {
            if (getProfile(player).getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate(message));
            }
        });
    }

    /**
     * Notifies the party members excluding the leader based on their profile settings.
     *
     * @param message The message to send.
     */
    public void notifyPartyExcludeLeader(String message) {
        this.getPlayersInParty().stream().filter(player -> !player.equals(this.leader)).forEach(player -> {
            if (getProfile(player).getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate(message));
            }
        });
    }

    /**
     * Gets the profile of a player.
     *
     * @param player The player to get the profile of.
     * @return The profile of the player.
     */
    private Profile getProfile(Player player) {
        return Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
    }

    /**
     * Registers the party.
     */
    private void registerParty() {
        Alley.getInstance().getPartyRepository().getParties().add(this);
    }

    /**
     * Unregisters the party.
     */
    private void unregisterParty() {
        Alley.getInstance().getPartyRepository().getParties().remove(this);
    }
}