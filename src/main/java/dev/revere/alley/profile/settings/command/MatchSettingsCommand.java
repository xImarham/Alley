package dev.revere.alley.profile.settings.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.settings.menu.MatchSettingsMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */

public class MatchSettingsCommand extends BaseCommand {
    @Override
    @CommandData(name = "matchsettings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new MatchSettingsMenu().openMenu(player);
    }
}
