package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.party.Party;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.party.PartyRequest;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 18:33
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

        String target = args[0];
        Player targetPlayer = Bukkit.getPlayer(target);

        if (targetPlayer == null) {
            player.sendMessage(CC.translate(ErrorMessage.PLAYER_NOT_ONLINE.replace("{player}", target)));
            return;
        }

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();
        PartyRequest partyRequest = partyRepository.getRequest(player);

        if (partyRequest == null || !partyRequest.getSender().equals(targetPlayer)) {
            player.sendMessage(CC.translate("&cYou do not have a party invitation from " + targetPlayer.getName() + "."));
            return;
        }

        Party party = partyRepository.getPartyByLeader(targetPlayer);
        if (party != null) {
            partyRepository.joinParty(player, targetPlayer);
            partyRepository.removeRequest(partyRequest);
            player.sendMessage(CC.translate("&aYou have joined " + targetPlayer.getName() + "'s party."));
        } else {
            player.sendMessage(CC.translate("&cThe party led by " + targetPlayer.getName() + " no longer exists."));
        }
    }
}
