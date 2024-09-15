package me.emmy.alley.match.command.player;

import me.emmy.alley.match.menu.CurrentMatchesMenu;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class CurrentMatchesCommand extends BaseCommand {
    @Command(name = "currentmatches", aliases = {"matches", "games", "currentgames"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new CurrentMatchesMenu().openMenu(player);
    }
}
