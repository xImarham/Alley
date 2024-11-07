package dev.revere.alley.profile.settings.command;

import dev.revere.alley.profile.settings.menu.SettingsMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
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
