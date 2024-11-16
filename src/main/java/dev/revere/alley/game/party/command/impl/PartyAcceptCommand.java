package dev.revere.alley.game.party.command.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.game.party.PartyRequest;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:33
 */
public class PartyAcceptCommand extends BaseCommand {
    @Override
    @Command(name = "party.accept", aliases = "p.accept")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /party accept (party-owner)"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(CC.translate(ErrorMessage.PLAYER_NOT_ONLINE.replace("{player}", args[0])));
            return;
        }

        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();

        Party party = partyHandler.getPartyByLeader(target);
        if (party == null) {
            player.sendMessage(CC.translate(ErrorMessage.TARGET_HAS_NO_PARTY.replace("{player}", target.getName())));
            return;
        }

        PartyRequest partyRequest = partyHandler.getRequest(player);
        if (partyRequest == null || !partyRequest.getSender().equals(target)) {
            player.sendMessage(CC.translate(Locale.NO_PARTY_INVITE.getMessage().replace("{player}", target.getName())));
            return;
        }

        if (partyRequest.hasExpired()) {
            partyHandler.removeRequest(partyRequest);
            player.sendMessage(CC.translate("&cThe party request has expired."));
            return;
        }

        partyHandler.joinParty(player, target);
        partyHandler.removeRequest(partyRequest);
        player.sendMessage(CC.translate(Locale.JOINED_PARTY.getMessage().replace("{player}", target.getName())));
    }
}