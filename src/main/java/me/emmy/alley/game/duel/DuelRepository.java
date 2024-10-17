package me.emmy.alley.game.duel;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.chat.CC;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
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
public class DuelRepository {
    private List<DuelRequest> duelRequestRequests = new ArrayList<>();

    /**
     * Add duel request to the list of duel requests.
     *
     * @param duelRequest the duel
     */
    public void addDuelRequest(DuelRequest duelRequest) {
        duelRequestRequests.add(duelRequest);
    }

    /**
     * Remove duel request from the list of duel requests.
     *
     * @param duelRequest the duel
     */
    public void removeDuelRequest(DuelRequest duelRequest) {
        duelRequestRequests.remove(duelRequest);
    }

    /**
     * Get duel request.
     *
     * @param sender the sender
     * @param target the target
     * @return the duel request
     */
    public DuelRequest getDuelRequest(Player sender, Player target) {
        for (DuelRequest duelRequest : duelRequestRequests) {
            if (duelRequest.getSender().equals(sender) && duelRequest.getTarget().equals(target)) {
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
     */
    public void sendDuelRequest(Player sender, Player target, Kit kit) {
        DuelRequest duelRequest = new DuelRequest(sender, target, kit, Alley.getInstance().getArenaRepository().getRandomArena(kit));
        this.addDuelRequest(duelRequest);

        TextComponent invitation = new TextComponent(CC.translate("&b" + sender.getName() + " &ahas challenged you to a duel! [CLICK TO ACCEPT]"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + sender.getName()));

        String hover = CC.translate("&aClick to accept " + sender.getName() + "&a's duel challenge.");
        TextComponent hoverComponent = new TextComponent(hover);
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverComponent}));

        target.spigot().sendMessage(invitation);
    }
}