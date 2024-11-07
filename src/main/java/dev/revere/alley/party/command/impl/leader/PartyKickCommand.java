package dev.revere.alley.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.party.Party;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
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