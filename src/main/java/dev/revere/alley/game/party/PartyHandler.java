package dev.revere.alley.game.party;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.hotbar.HotbarRepository;
import dev.revere.alley.hotbar.enums.HotbarType;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 22:57
 */
@Getter
@Setter
public class PartyHandler {
    private final List<PartyRequest> partyRequests;
    private final List<Party> parties;
    private String chatFormat;

    public PartyHandler() {
        this.partyRequests = new ArrayList<>();
        this.parties = new ArrayList<>();
        this.chatFormat = Alley.getInstance().getConfigHandler().getConfig("messages.yml").getString("party.chat-format");
    }

    /**
     * Creates a party.
     *
     * @param player The leader of the party.
     */
    public void createParty(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot create a party in this state."));
            return;
        }

        Party party = new Party(player);
        profile.setParty(party);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.PARTY);
    }

    /**
     * Joins a party.
     *
     * @param player The player to join the party.
     * @param leader The leader of the party.
     */
    public void joinParty(Player player, Player leader) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis party does not exist."));
            return;
        }

        if (party.getMembers().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are already in this party."));
            return;
        }

        party.getMembers().add(player.getUniqueId());
        this.notifyParty(party, "&a" + player.getName() + " has joined the party.");
        this.setupProfile(player, true);
    }

    /**
     * Disbands the party.
     *
     * @param leader The leader of the party.
     */
    public void disbandParty(Player leader) {
        Party party = this.getPartyByLeader(leader);
        party.getMembers().forEach(member -> this.setupProfile(Bukkit.getPlayer(member), false));
        this.notifyPartyExcludeLeader(party, "&cThe party has been disbanded.");
        this.getParties().remove(party);
        this.setupProfile(leader, false);
    }

    /**
     * Sets up the profile of a player.
     *
     * @param player The player to set up the profile for.
     * @param join   Whether the player is joining a party.
     */
    private void setupProfile(Player player, boolean join) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        HotbarRepository hotbarRepository = Alley.getInstance().getHotbarRepository();

        profile.setParty(join ? this.getPartyByMember(player.getUniqueId()) : null);
        if (join && (profile.getState() == EnumProfileState.LOBBY || profile.getState() == EnumProfileState.WAITING)) {
            hotbarRepository.applyHotbarItems(player, HotbarType.PARTY);
        } else {
            hotbarRepository.applyHotbarItems(player, HotbarType.LOBBY);
        }
    }

    /**
     * Removes a member from the party.
     *
     * @param player The member to remove.
     */
    public void leaveParty(Player player) {
        Party party = this.getPartyByMember(player.getUniqueId());
        if (party == null) {
            if (player.isOnline()) {
                player.sendMessage(CC.translate("&cYou are not in a party."));
            }
            return;
        }

        party.getMembers().remove(player.getUniqueId());
        this.notifyParty(party, "&a" + player.getName() + " has left the party.");
        this.setupProfile(player, false);
    }

    /**
     * Kicks a member from the party.
     *
     * @param leader The leader of the party.
     * @param member The member to kick.
     */
    public void kickMember(Player leader, Player member) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) return;
        party.getMembers().remove(member.getUniqueId());
        this.notifyParty(party, "&c" + member.getName() + " has been kicked from the party.");
        this.setupProfile(member, false);
    }

    /**
     * Adds a party request to the repository.
     *
     * @param request The party request to add.
     */
    public void addRequest(PartyRequest request) {
        this.partyRequests.add(request);
    }

    /**
     * Gets a party request for a specific player.
     *
     * @param player The player to get the request for.
     * @return The party request for the player.
     */
    public PartyRequest getRequest(Player player) {
        return this.partyRequests.stream()
                .filter(request -> request.getTarget().equals(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a party request from the repository.
     *
     * @param request The party request to remove.
     */
    public void removeRequest(PartyRequest request) {
        this.partyRequests.remove(request);
    }

    /**
     * Sends a party request to the target player.
     *
     * @param party  The party to send the request to.
     * @param target The target player to send the request to.
     */
    public void sendRequest(Party party, Player target) {
        this.notifyParty(party, "&b" + target.getName() + " &ahas been invited to the party.");

        String partyLeader = party.getLeader().getName();

        TextComponent invitation = new TextComponent(CC.translate(" &a(Click To Accept)"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + partyLeader));

        String hover = CC.translate("&aClick to accept " + partyLeader + "&a's party invitation.");
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));

        this.sendInvite(party.getLeader(), target, invitation);
    }

    /**
     * Sends an invite message to a player.
     *
     * @param sender      The player sending the invite.
     * @param target      The player receiving the invite.
     * @param invitation  The invitation message.
     */
    private void sendInvite(Player sender, Player target, TextComponent invitation) {
        target.sendMessage("");
        target.sendMessage(CC.translate("&b&lParty Invitation"));
        target.sendMessage(CC.translate("&f&l ● &fFrom: &b" + sender.getName()));
        //target.sendMessage(CC.translate("&f&l ● &fLeader: &b" + this.getPartyByMember(sender.getUniqueId()).getLeader().getName()));
        target.spigot().sendMessage(invitation);
        target.sendMessage("");
    }

    /**
     * Notifies the party members based on their profile settings.
     *
     * @param party   The party to notify.
     * @param message The message to send.
     */
    public void notifyParty(Party party, String message) {
        party.getMembers().stream().map(uuid -> Alley.getInstance().getServer().getPlayer(uuid)).forEach(player -> {
            if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate(message));
            }
        });
    }

    /**
     * Notifies the party members excluding the leader based on their profile settings.
     *
     * @param message The message to send.
     */
    public void notifyPartyExcludeLeader(Party party, String message) {
        party.getMembers().stream().filter(uuid -> !party.getLeader().getUniqueId().equals(uuid)).map(uuid -> Alley.getInstance().getServer().getPlayer(uuid)).forEach(player -> {
            if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate(message));
            }
        });
    }

    /**
     * Gets the party of a leader.
     *
     * @param player The leader of the party.
     * @return The party of the leader.
     */
    public Party getPartyByLeader(Player player) {
        return this.parties.stream()
                .filter(party -> party.getLeader().equals(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the party of a member.
     *
     * @param uuid The member's UUID.
     * @return The party of the member.
     */
    public Party getPartyByMember(UUID uuid) {
        return this.parties.stream()
                .filter(party -> party.getMembers().contains(uuid))
                .findFirst()
                .orElse(null);
    }
}