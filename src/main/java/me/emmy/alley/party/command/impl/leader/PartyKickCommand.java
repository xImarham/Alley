package me.emmy.alley.party.command.impl.leader;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.Party;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/05/2024 - 19:05
 */
public class PartyKickCommand extends BaseCommand {
    @Override
    @Command(name = "party.kick", aliases = "p.kick")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /party kick (player)"));
            return;
        }

        String target = args[0];

        if (target == null) {
            player.sendMessage(CC.translate(ErrorMessage.PLAYER_NOT_ONLINE).replace("{player}", args[0]));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(target);
        Party party = Alley.getInstance().getPartyRepository().getPartyByLeader(player);

        if (party == null) {
            player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
            return;
        }

        party.kickPlayer(targetPlayer);

    }
}
