package me.emmy.alley.kit.command.impl.settings;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
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
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("     &d&lKit Settings List &f(" + Alley.getInstance().getKitSettingRepository().getSettings().size() + "&f)"));
        if (Alley.getInstance().getKitSettingRepository().getSettings().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Kit Settings available."));
        }
        Alley.getInstance().getKitSettingRepository().getSettings().forEach(setting -> player.sendMessage(CC.translate("      &f● &d" + setting.getName())));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");
    }
}
