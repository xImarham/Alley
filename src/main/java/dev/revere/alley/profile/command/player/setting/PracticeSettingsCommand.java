package dev.revere.alley.profile.command.player.setting;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.menu.setting.PracticeSettingsMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */

public class PracticeSettingsCommand extends BaseCommand {
    @Override
    @CommandData(name = "practicesettings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new PracticeSettingsMenu().openMenu(player);
    }
}
