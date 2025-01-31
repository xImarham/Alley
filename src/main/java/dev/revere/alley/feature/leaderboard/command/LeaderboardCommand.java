package dev.revere.alley.feature.leaderboard.command;

import dev.revere.alley.feature.leaderboard.menu.LeaderboardMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:29
 */
public class LeaderboardCommand extends BaseCommand {
    @Override
    @Command(name = "leaderboard", aliases = {"leaderboards", "lb"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new LeaderboardMenu().openMenu(player);
    }
}
