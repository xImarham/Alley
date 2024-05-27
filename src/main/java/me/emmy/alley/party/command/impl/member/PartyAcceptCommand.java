package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.Party;
import me.emmy.alley.party.PartyRepository;
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
    @Command(name = "party.accept")
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

        //TODO: Check if the target has sent an invite
        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();
        if (partyRepository.getPartyByLeader(targetPlayer) != null) {
            partyRepository.joinParty(player, targetPlayer);
            return;
        }
    }
}
