package dev.revere.alley.game.duel;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
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
     * @param sender the sender
     * @param target the target
     * @param kit    the kit
     */
    public void sendDuelRequest(Player sender, Player target, Kit kit) {
        AbstractArena arena = this.plugin.getArenaService().getRandomArena(kit);
        DuelRequest duelRequest = new DuelRequest(sender, target, kit, arena);
        this.addDuelRequest(duelRequest);

        this.sendInvite(sender, target, kit, arena, this.getClickable(sender));
    }

    /**
     * Send duel request to the target player with a specific arena.
     *
     * @param sender the sender
     * @param target the target
     * @param kit    the kit
     * @param arena  the arena
     */
    public void sendDuelRequest(Player sender, Player target, Kit kit, AbstractArena arena) {
        DuelRequest duelRequest = new DuelRequest(sender, target, kit, arena);
        this.addDuelRequest(duelRequest);

        this.sendInvite(sender, target, kit, arena, this.getClickable(sender));
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
        target.sendMessage(CC.translate("&f&l ● &fArena: &b" + arena.getName()));
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
        if (duelRequest.hasExpired()) return;

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