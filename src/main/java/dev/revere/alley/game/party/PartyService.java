package dev.revere.alley.game.party;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cooldown.Cooldown;
import dev.revere.alley.feature.cooldown.enums.EnumCooldownType;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
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
public class PartyService {
    private final List<Party> parties;
    private final List<PartyRequest> partyRequests;
    private String chatFormat;

    public PartyService() {
        this.parties = new ArrayList<>();
        this.partyRequests = new ArrayList<>();
        this.chatFormat = Alley.getInstance().getConfigService().getConfig("messages.yml").getString("party.chat-format");
    }

    /**
     * Method to create a new party.
     *
     * @param player The leader of the party.
     */
    public void createParty(Player player) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot create a party in this state."));
            return;
        }

        Party party = new Party(player);
        profile.setParty(party);
        Alley.getInstance().getHotbarService().applyHotbarItems(player, HotbarType.PARTY);
    }

    /**
     * Bans a member from the party.
     *
     * @param leader The leader of the party.
     * @param target The member to ban.
     */
    public void banMember(Player leader, Player target) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (!party.getMembers().contains(target.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not in your party."));
            return;
        }

        party.getBannedPlayers().add(target.getUniqueId());
        party.getMembers().remove(target.getUniqueId());
        this.setupProfile(target, false);

        party.notifyParty(CC.translate("&c" + target.getName() + " has been banned from the party."));
        target.sendMessage(CC.translate("&cYou have been banned from the party."));
    }

    /**
     * Unbans a member from the party.
     *
     * @param leader The leader of the party.
     * @param target The member to unban.
     */
    public void unbanMember(Player leader, Player target) {
        Party party = this.getPartyByLeader(leader);
        if (party == null) {
            leader.sendMessage(CC.translate("&cYou are not the leader of a party."));
            return;
        }

        if (!party.getBannedPlayers().contains(target.getUniqueId())) {
            leader.sendMessage(CC.translate("&cThat player is not banned from your party."));
            return;
        }

        party.getBannedPlayers().remove(target.getUniqueId());
        party.notifyParty(CC.translate("&b" + target.getName() + " &ahas been unbanned from the party and is now able to join again."));
        target.sendMessage(CC.translate("&aYou have been unbanned from &b" + party.getLeader().getName() + "'s &aparty."));
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

        if (party.getBannedPlayers().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are banned from &b" + leader.getName() + "&c's party."));
            return;
        }

        party.getMembers().add(player.getUniqueId());
        party.notifyParty("&a" + player.getName() + " has joined the party.");
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
        party.notifyPartyExcludeLeader("&cThe party has been disbanded.");
        this.parties.remove(party);

        Cooldown cooldown = Alley.getInstance().getCooldownRepository().getCooldown(leader.getUniqueId(), EnumCooldownType.PARTY_ANNOUNCE_COOLDOWN);
        if (cooldown != null && cooldown.isActive()) {
            cooldown.resetCooldown();
        }

        if (leader.isOnline()) {
            this.setupProfile(leader, false);
        }
    }

    /**
     * Sets up the profile of a player.
     *
     * @param player The player to set up the profile for.
     * @param join   Whether the player is joining a party.
     */
    private void setupProfile(Player player, boolean join) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        HotbarService hotbarService = Alley.getInstance().getHotbarService();

        profile.setParty(join ? this.getPartyByMember(player.getUniqueId()) : null);
        if (join && (profile.getState() == EnumProfileState.LOBBY || profile.getState() == EnumProfileState.WAITING)) {
            hotbarService.applyHotbarItems(player, HotbarType.PARTY);
        } else {
            hotbarService.applyHotbarItems(player, HotbarType.LOBBY);
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
        party.notifyParty("&a" + player.getName() + " has left the party.");
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
        party.notifyParty("&c" + member.getName() + " has been kicked from the party.");
        this.setupProfile(member, false);
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
     * Creates a new party request and send a message to the target.
     *
     * @param party  The party to send the invite to.
     * @param sender The player sending the invite.
     * @param target The player receiving the invite.
     */
    public void sendInvite(Party party, Player sender, Player target) {
        if (party == null) return;

        PartyRequest request = new PartyRequest(sender, target);
        this.partyRequests.add(request);

        target.sendMessage("");
        target.sendMessage(CC.translate("&b&lParty Invitation"));
        target.sendMessage(CC.translate("&f&l ● &fYou've been invited to join &b" + party.getLeader().getName() + "&f's party."));
        target.sendMessage(CC.translate("&f&l ● &fFrom: &b" + sender.getName()));
        target.sendMessage(CC.translate("&f&l ● &fPlayers: &b" + party.getMembers().size() + "&f/&b30")); //TODO: Implement party size limit with permissions ect...
        target.spigot().sendMessage(this.getClickable(sender));
        target.sendMessage("");
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

    /**
     * Gets the party of a player, either as a leader or a member.
     *
     * @param player The player to check.
     * @return The party the player is in, or null if the player is not in any party.
     */
    public Party getParty(Player player) {
        Party party = getPartyByLeader(player);
        if (party != null) {
            return party;
        }
        return getPartyByMember(player.getUniqueId());
    }

    /**
     * Announces a party to all online players.
     *
     * @param party The party to announce.
     */
    public void announceParty(Party party) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("");
            player.sendMessage(CC.translate("&b&l" + party.getLeader().getName() + " &a&lis inviting you to join &b&ltheir &a&lparty!"));
            player.spigot().sendMessage(this.getClickable(party.getLeader()));
            player.sendMessage("");
        });
    }

    /**
     * Gets the clickable text component for accepting a party invitation.
     *
     * @param sender The player who sent the invitation.
     * @return The clickable text component.
     */
    private TextComponent getClickable(Player sender) {
        return ClickableUtil.createComponent(
                " &a(Click to accept)",
                "/party join " + sender.getName(),
                "&aClick to accept &b" + sender.getName() + "&a's party invitation."
        );
    }
}