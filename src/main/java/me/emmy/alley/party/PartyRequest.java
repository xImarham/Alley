package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.utils.chat.CC;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:44
 */
@Getter
@Setter
public class PartyRequest {

    private final List<PartyRequest> partyRequest = new ArrayList<>();

    private final Player sender;
    private final Player target;

    /**
     * Constructor for the PartyRequest class.
     *
     * @param sender The player sending the request.
     * @param target The player receiving the request.
     */
    public PartyRequest(Player sender, Player target) {
        this.sender = sender;
        this.target = target;
    }

    /**
     * Sends a party request to the target player.
     *
     * @param party  The party to send the request to.
     * @param target The target player to send the request to.
     */
    public void sendRequest(Party party, Player target) {
        party.notifyParty("&b" + target.getName() + " &ahas been invited to the party.");

        String partyLeader = party.getLeader().getName();

        TextComponent invitation = new TextComponent(CC.translate("&b" + partyLeader + " &ahas invited you to their party! [CLICK TO JOIN]"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + partyLeader));

        String hover = CC.translate("&aClick to accept " + partyLeader + "&a's party invitation.");
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));

        target.spigot().sendMessage(invitation);
    }
}
