package dev.revere.alley.kit.command.impl.settings;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSettingsCommand extends BaseCommand {
    @Command(name = "kit.settings", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lKit Settings List &f(" + Alley.getInstance().getKitSettingRepository().getSettings().size() + "&f)"));
        if (Alley.getInstance().getKitSettingRepository().getSettings().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Kit Settings available."));
        }
        Alley.getInstance().getKitSettingRepository().getSettings().forEach(setting -> player.sendMessage(CC.translate("      &f● &b" + setting.getName())));
        player.sendMessage("");
    }
}