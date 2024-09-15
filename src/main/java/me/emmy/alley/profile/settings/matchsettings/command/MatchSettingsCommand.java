package me.emmy.alley.profile.settings.matchsettings.command;

import me.emmy.alley.profile.settings.matchsettings.menu.MatchSettingsMenu;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */

public class MatchSettingsCommand extends BaseCommand {
    @Override
    @Command(name = "matchsettings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new MatchSettingsMenu().openMenu(player);
    }
}
