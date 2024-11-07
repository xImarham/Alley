package dev.revere.alley.game.ffa.command.admin;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAListCommand extends BaseCommand {
    @Command(name = "ffa.list", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lFFA Match List &f(" + Alley.getInstance().getFfaRepository().getMatches().size() + "&f)"));
        if (Alley.getInstance().getFfaRepository().getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Matches available."));
        }
        Alley.getInstance().getFfaRepository().getMatches().forEach(match -> player.sendMessage(CC.translate("      &f● &b" + match.getKit().getDisplayName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage("");
    }
}
