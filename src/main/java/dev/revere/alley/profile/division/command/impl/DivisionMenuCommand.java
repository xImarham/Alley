package dev.revere.alley.profile.division.command.impl;

import dev.revere.alley.profile.division.menu.DivisionsMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionMenuCommand extends BaseCommand {
    @Command(name = "division.menu", aliases = {"divisions"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new DivisionsMenu().openMenu(player);
    }
}
