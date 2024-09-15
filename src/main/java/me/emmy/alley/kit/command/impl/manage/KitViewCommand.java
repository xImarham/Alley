package me.emmy.alley.kit.command.impl.manage;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitViewCommand extends BaseCommand {
    @Command(name = "kit.view", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /kit view <kit>"));
            return;
        }

        String kitName = args[0];
        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lKit " + kit.getName() +  " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        player.sendMessage(CC.translate("      &f● &bDisplay Name: &f" + kit.getDisplayName()));
        player.sendMessage(CC.translate("      &f● &bName: &f" + kit.getName()));
        player.sendMessage(CC.translate("      &f● &bDescription: &f" + kit.getDescription()));
        player.sendMessage(CC.translate("      &f● &bSettings: &f"));
        kit.getKitSettings().forEach(setting -> player.sendMessage(CC.translate("        &f● &b" + setting.getName() + " &f(" + (setting.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)")));
        player.sendMessage("");
    }
}
