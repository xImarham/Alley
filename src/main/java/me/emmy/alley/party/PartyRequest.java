package me.emmy.alley.party;

import lombok.Getter;
import me.emmy.alley.utils.chat.CC;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 18:44
 */

public class PartyRequest {
    public void sendRequest(Party party, Player target) {
        party.notifyParty("&b" + target.getName() + " &ahas been invited to the party.");

        String partyLeader = party.getLeader().getName();

        TextComponent invitation = new TextComponent(CC.translate("&b" + partyLeader + " &ahas invited you to their party! [CLICK TO JOIN]"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + partyLeader));

        String hover = CC.translate("&aClick to join " + partyLeader + "&a's party.");
        BaseComponent[] hoverComponentW = new ComponentBuilder(hover).create();
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponentW));

        target.spigot().sendMessage(invitation);
    }
}
