package dev.revere.alley.game.duel;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:02
 */
@Getter
@Setter
public class DuelRequestService {
    protected final Alley plugin;
    private final List<DuelRequest> duelRequests;

    /**
     * Constructor for the DuelRequestService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public DuelRequestService(Alley plugin) {
        this.plugin = plugin;
        this.duelRequests = new ArrayList<>();
    }

    /**
     * Add duel request to the list of duel requests.
     *
     * @param duelRequest the duel
     */
    public void addDuelRequest(DuelRequest duelRequest) {
        this.duelRequests.add(duelRequest);
    }

    /**
     * Remove duel request from the list of duel requests.
     *
     * @param duelRequest the duel
     */
    public void removeDuelRequest(DuelRequest duelRequest) {
        this.duelRequests.remove(duelRequest);
    }

    /**
     * Get a duel request by the sender and target.
     *
     * @param sender the sender
     * @param target the target
     * @return the duel request
     */
    public DuelRequest getDuelRequest(Player sender, Player target) {
        for (DuelRequest duelRequest : this.duelRequests) {
            if (duelRequest.getSender().equals(sender) && duelRequest.getTarget().equals(target) || (duelRequest.getSender().equals(target) && duelRequest.getTarget().equals(sender))) {
                return duelRequest;
            }
        }
        return null;
    }

    /**
     * Send duel request to the target player.
     *
     * @param player the sender
     * @param target the target
     * @param kit    the kit
     */
    public void sendDuelRequest(Player player, Player target, Kit kit, boolean party) {
        if (isInvalidRequest(player, target)) {
            return;
        }
        AbstractArena arena = this.plugin.getArenaService().getRandomArena(kit);
        DuelRequest duelRequest = new DuelRequest(player, target, kit, arena, party);
        this.addDuelRequest(duelRequest);

        this.sendInvite(player, target, kit, arena, this.getClickable(player));
    }

    /**
     * Send duel request to the target player with a specific arena.
     *
     * @param player the sender
     * @param target the target
     * @param kit    the kit
     * @param arena  the arena
     */
    public void sendDuelRequest(Player player, Player target, Kit kit, AbstractArena arena, boolean party) {
        if (isInvalidRequest(player, target)) {
            return;
        }
        if (arena instanceof StandAloneArena) {
            arena = plugin.getArenaService().getTemporaryArena(arena);
        }

        DuelRequest duelRequest = new DuelRequest(player, target, kit, arena, party);
        this.addDuelRequest(duelRequest);

        this.sendInvite(player, target, kit, arena, this.getClickable(player));
    }

    /**
     * Send invite to the target player.
     *
     * @param sender     the sender
     * @param target     the target
     * @param kit        the kit
     * @param arena      the arena
     * @param invitation the invitation
     */
    private void sendInvite(Player sender, Player target, Kit kit, AbstractArena arena, TextComponent invitation) {
        target.sendMessage("");
        target.sendMessage(CC.translate("&b&lDuel Request"));
        target.sendMessage(CC.translate("&f&l ● &fFrom: &b" + sender.getName()));
        target.sendMessage(CC.translate("&f&l ● &fArena: &b" + arena.getDisplayName()));
        target.sendMessage(CC.translate("&f&l ● &fKit: &b" + kit.getName()));
        target.spigot().sendMessage(invitation);
        target.sendMessage("");
    }

    /**
     * Accept a pending duel request.
     *
     * @param duelRequest the duel
     */
    public void acceptPendingRequest(DuelRequest duelRequest) {
        if (!isValidAcceptRequest(duelRequest)) {
            return;
        }

        MatchGamePlayerImpl playerA = new MatchGamePlayerImpl(duelRequest.getSender().getUniqueId(), duelRequest.getSender().getName());
        MatchGamePlayerImpl playerB = new MatchGamePlayerImpl(duelRequest.getTarget().getUniqueId(), duelRequest.getTarget().getName());

        GameParticipant<MatchGamePlayerImpl> participantA = new GameParticipant<>(playerA);
        GameParticipant<MatchGamePlayerImpl> participantB = new GameParticipant<>(playerB);

        this.plugin.getMatchService().createAndStartMatch(
                duelRequest.getKit(), duelRequest.getArena(), participantA, participantB, false, false, false
        );

        this.removeDuelRequest(duelRequest);
    }

    /**
     * Checks if the duel request is valid.
     *
     * @param player the player sending the request
     * @param target the target player
     * @return true if the request is invalid, false otherwise
     */
    private boolean isInvalidRequest(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return true;
        }

        if (target == player) {
            player.sendMessage(CC.translate("&cYou cannot duel yourself."));
            return true;
        }

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to duel a player."));
            return true;
        }

        Profile targetProfile = this.plugin.getProfileService().getProfile(target.getUniqueId());
        if (targetProfile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cThat player is not in the lobby."));
            return true;
        }

        if (targetProfile.getParty() != null && profile.getParty() == null) {
            player.sendMessage(CC.translate("&cThat player is in a party and you're not. You can't duel them."));
            return true;
        }

        if (targetProfile.getParty() == null && profile.getParty() != null) {
            player.sendMessage(CC.translate("&cYou are in a party and the target player is not. You can't duel them."));
            return true;
        }

        if (targetProfile.getParty() != null && profile.getParty().getMembers().contains(target.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou cannot duel a member of your own party."));
            return true;
        }

        return false;
    }

    /**
     * Checks if the accept request is valid.
     *
     * @param duelRequest the duel request
     * @return true if the request is valid, false otherwise
     */
    private boolean isValidAcceptRequest(DuelRequest duelRequest) {
        if (duelRequest.getSender() == null || duelRequest.getTarget() == null) {
            return false;
        }

        if (this.plugin.getServerService().isQueueingEnabled(duelRequest.getSender())) {
            return false;
        }

        if (duelRequest.hasExpired()) return false;

        Profile profile = this.plugin.getProfileService().getProfile(duelRequest.getSender().getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou can only accept duel requests in the lobby."));
            return false;
        }

        if (duelRequest.getTarget() == null) {
            duelRequest.getSender().sendMessage(CC.translate("&cThat player is not online."));
            return false;
        }

        Profile targetProfile = this.plugin.getProfileService().getProfile(duelRequest.getTarget().getUniqueId());
        if (targetProfile.getState() != EnumProfileState.LOBBY) {
            duelRequest.getSender().sendMessage(CC.translate("&cThat player is not in the lobby."));
            return false;
        }

        if (targetProfile.getParty() != null && profile.getParty() == null) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou cannot accept a duel request from a player in a party if you are not in a party."));
            return false;
        }

        if (targetProfile.getParty() != null && targetProfile.getParty().getMembers().contains(duelRequest.getSender().getUniqueId())) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou cannot accept a duel request from a player in your party."));
            return false;
        }

        if (targetProfile.getParty() == null && profile.getParty() != null) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou cannot accept a duel request from a player who is not in a party."));
            return false;
        }

        if (duelRequest.isParty() && profile.getParty() == null) {
            duelRequest.getSender().sendMessage(CC.translate("&cYou can only accept party duel requests if you are in a party."));
            return false;
        }
        return true;
    }

    /**
     * Get the clickable component for the duel request.
     *
     * @param sender the sender
     * @return the clickable component
     */
    private TextComponent getClickable(Player sender) {
        return ClickableUtil.createComponent(
                " &a(Click To Accept)",
                "/accept " + sender.getName(),
                "&aClick to accept &b" + sender.getName() + "&a's duel request."
        );
    }
}