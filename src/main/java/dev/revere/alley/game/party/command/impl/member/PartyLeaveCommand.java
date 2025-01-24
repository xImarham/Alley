package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyLeaveCommand extends BaseCommand {
    @Override
    @Command(name = "party.leave", aliases = {"p.leave"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
        if (partyHandler.getPartyByLeader(player) != null) {
            partyHandler.disbandParty(player);
            player.sendMessage(CC.translate(Locale.PARTY_DISBANDED.getMessage()));
            return;
        }

        if (partyHandler.getPartyByMember(playerUUID) != null) {
            partyHandler.leaveParty(player);
            player.sendMessage(CC.translate(Locale.PARTY_LEFT.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
    }
}