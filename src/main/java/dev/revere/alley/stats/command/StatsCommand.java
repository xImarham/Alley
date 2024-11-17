package dev.revere.alley.stats.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.stats.menu.StatisticsMenu;
import dev.revere.alley.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 12:07
 */
public class StatsCommand extends BaseCommand {
    @Command(name = "stats", aliases = {"statistics"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            new StatisticsMenu(player).openMenu(player);
            return;
        }

        Player onlineTarget = Bukkit.getPlayerExact(args[0]);
        OfflinePlayer target = onlineTarget != null ? onlineTarget : PlayerUtil.getOfflinePlayerByName(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        new StatisticsMenu(target).openMenu(player);
    }
}