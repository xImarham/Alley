package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:56
 */
public class KitViewCommand extends BaseCommand {
    @CommandData(name = "kit.view", permission = "alley.admin.view")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit view &b<kitName>"));
            return;
        }

        Kit kit = Alley.getInstance().getKitService().getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lKit " + kit.getName() +  " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        player.sendMessage(CC.translate(" &f● &bDisplay Name: &f" + kit.getDisplayName()));
        player.sendMessage(CC.translate(" &f● &bName: &f" + kit.getName()));
        player.sendMessage(CC.translate(" &f● &bIcon: &f" + kit.getIcon().name().toLowerCase() + " &7(" + kit.getIconData() + ")"));
        player.sendMessage(CC.translate(" &f● &bDisclaimer: &f" + kit.getDisclaimer()));
        player.sendMessage(CC.translate(" &f● &bDescription: &f" + kit.getDescription()));
        player.spigot().sendMessage(ClickableUtil.createComponent(
                        "  &a(Click here to view the kit settings)",
                        "/kit viewsettings " + kit.getName(),
                        "&7Click to view the settings of the kit &b" + kit.getName())
        );
        player.sendMessage("");
    }
}