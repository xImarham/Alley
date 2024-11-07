package dev.revere.alley.game.match.command.player;

import dev.revere.alley.game.match.menu.CurrentMatchesMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
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