package me.emmy.alley.profile.command;

import me.emmy.alley.profile.menu.ProfileMenu;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 22:43
 */
public class ProfileMenuCommand extends BaseCommand {
    @Override
    @Command(name = "profilemenu", aliases = {"profilesettings"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new ProfileMenu().openMenu(player);
    }
}
