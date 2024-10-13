package me.emmy.alley.profile.settings.command;

import me.emmy.alley.profile.settings.menu.SettingsMenu;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */

public class PracticeSettingsCommand extends BaseCommand {
    @Override
    @Command(name = "practicesettings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new SettingsMenu().openMenu(player);
    }
}
