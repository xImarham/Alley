package me.emmy.alley.kit.command.impl.settings;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class KitSetSettingCommand extends BaseCommand {
    @Command(name = "kit.setting", aliases = {"kit.setsetting"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&cUsage: /kit setting <kit> <setting> <enabled>"));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        String settingName = args[1];
        boolean enabled = Boolean.parseBoolean(args[2]);

        if (kit.getKitSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null) == null) {
            player.sendMessage(CC.translate("&cA setting with that name does not exist."));
            return;
        }

        kit.getKitSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(settingName)).findFirst().ifPresent(setting -> setting.setEnabled(enabled));
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate("&aSuccessfully set the setting &b" + settingName + " &ato &b" + enabled + " &afor the kit &b" + kit.getName() + "&a."));
    }
}
