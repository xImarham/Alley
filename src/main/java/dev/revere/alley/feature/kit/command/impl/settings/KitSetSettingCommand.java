package dev.revere.alley.feature.kit.command.impl.settings;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class KitSetSettingCommand extends BaseCommand {
    @CommandData(name = "kit.setsetting", aliases = {"kit.setting"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setsetting &b<kit> <setting> <true/false>"));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        String settingName = args[1];
        boolean enabled = Boolean.parseBoolean(args[2]);

        if (Alley.getInstance().getKitSettingRepository().getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null) == null) {
            player.sendMessage(CC.translate("&cA setting with that name does not exist."));
            return;
        }

        kit.getKitSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(settingName)).findFirst().ifPresent(setting -> setting.setEnabled(enabled));
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_SETTING_SET.getMessage()).replace("{setting-name}", settingName).replace("{enabled}", String.valueOf(enabled)).replace("{kit-name}", kit.getName()));
    }
}