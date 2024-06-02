package me.emmy.alley.profile.division.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.division.menu.DivisionsMenu;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
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
