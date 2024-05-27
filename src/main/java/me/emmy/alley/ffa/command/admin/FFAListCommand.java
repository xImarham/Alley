package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
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
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("     &d&lFFA Match List &f(" + Alley.getInstance().getFfaRepository().getMatches().size() + "&f)"));
        if (Alley.getInstance().getFfaRepository().getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Matches available."));
        }
        Alley.getInstance().getFfaRepository().getMatches().forEach(match -> player.sendMessage(CC.translate("      &f● &d" + match.getKit().getDisplayName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");
    }
}
