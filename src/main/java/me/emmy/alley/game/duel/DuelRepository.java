package me.emmy.alley.game.duel;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.game.match.AbstractMatch;
import me.emmy.alley.game.match.impl.MatchLivesRegularImpl;
import me.emmy.alley.game.match.impl.MatchRegularImpl;
import me.emmy.alley.game.match.player.GameParticipant;
import me.emmy.alley.game.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.KitSettingLivesImpl;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.util.chat.CC;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:02
 */
@Getter
@Setter
public class DuelRepository {
    private final List<DuelRequest> duelRequests;

    /**
     * Constructor for the DuelRepository class which initializes the duel requests.
     */
    public DuelRepository() {
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
        Arena arena = Alley.getInstance().getArenaRepository().getRandomArena(kit);
        DuelRequest duelRequest = new DuelRequest(sender, target, kit, arena);
        this.addDuelRequest(duelRequest);

        TextComponent invitation = new TextComponent(CC.translate(" &a(Click To Accept)"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + sender.getName()));

        String hover = CC.translate("&aClick to accept " + sender.getName() + "&a's duel challenge.");
        TextComponent hoverComponent = new TextComponent(hover);
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverComponent}));

        this.sendInvite(sender, target, kit, arena, invitation);
    }

    /**
     * Send duel request to the target player.
     *
     * @param sender the sender
     * @param target the target
     * @param kit    the kit
     * @param arena  the arena
     */
    public void sendDuelRequest(Player sender, Player target, Kit kit, Arena arena) {
        DuelRequest duelRequest = new DuelRequest(sender, target, kit, arena);
        this.addDuelRequest(duelRequest);

        TextComponent invitation = new TextComponent(CC.translate(" &a(Click To Accept)"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + sender.getName()));

        String hover = CC.translate("&aClick to accept " + sender.getName() + "&a's duel challenge.");
        TextComponent hoverComponent = new TextComponent(hover);
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverComponent}));

        this.sendInvite(sender, target, kit, arena, invitation);
    }

    /**
     * Send invite to the target player.
     *
     * @param sender      the sender
     * @param target      the target
     * @param kit         the kit
     * @param arena       the arena
     * @param invitation  the invitation
     */
    private void sendInvite(Player sender, Player target, Kit kit, Arena arena, TextComponent invitation) {
        target.sendMessage("");
        target.sendMessage(CC.translate("&b&lDuel Request"));
        target.sendMessage(CC.translate("&f&l ● &fFrom: &b" + sender.getName()));
        target.sendMessage(CC.translate("&f&l ● &fArena: &b" + arena.getName()));
        target.sendMessage(CC.translate("&f&l ● &fKit: &b" + kit.getName()));
        target.sendMessage("");
        target.spigot().sendMessage(invitation);
        target.sendMessage("");
    }

    /**
     * Accept a pending duel request.
     *
     * @param duelRequest the duel
     */
    public void acceptPendingRequest(DuelRequest duelRequest) {
        MatchGamePlayerImpl playerA = new MatchGamePlayerImpl(duelRequest.getSender().getUniqueId(), duelRequest.getSender().getName());
        MatchGamePlayerImpl playerB = new MatchGamePlayerImpl(duelRequest.getTarget().getUniqueId(), duelRequest.getTarget().getName());

        GameParticipant<MatchGamePlayerImpl> participantA = new GameParticipant<>(playerA);
        GameParticipant<MatchGamePlayerImpl> participantB = new GameParticipant<>(playerB);

        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (queue.getKit().equals(duelRequest.getKit()) && !queue.isRanked()) {
                if (queue.getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                    AbstractMatch match = new MatchLivesRegularImpl(queue, duelRequest.getKit(), duelRequest.getArena(), false, participantA, participantB);
                    match.startMatch();
                } else {
                    AbstractMatch match = new MatchRegularImpl(queue, duelRequest.getKit(), duelRequest.getArena(), false, participantA, participantB);
                    match.startMatch();
                }
            }
        }

        this.removeDuelRequest(duelRequest);
    }
}