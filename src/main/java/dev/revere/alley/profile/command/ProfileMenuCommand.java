package dev.revere.alley.profile.command;

import dev.revere.alley.profile.menu.ProfileMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 22:43
 */
public class ProfileMenuCommand extends BaseCommand {
    @Override
    @Command(name = "profilemenu")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new ProfileMenu().openMenu(player);
    }
}
