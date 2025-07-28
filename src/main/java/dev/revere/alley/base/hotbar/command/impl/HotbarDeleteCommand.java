package dev.revere.alley.base.hotbar.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.hotbar.HotbarItem;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 26/07/2025
 */
public class HotbarDeleteCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.delete",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/hotbar delete &6<name>"));
            return;
        }

        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        String name = args[0];

        HotbarItem hotbarItem = hotbarService.getHotbarItem(name);
        if (hotbarItem == null) {
            player.sendMessage(CC.translate("&cNo hotbar item found with the name &e" + name + "&c."));
            return;
        }

        hotbarService.deleteHotbarItem(hotbarItem);
        player.sendMessage(CC.translate("&aHotbar item &e" + name + " &adeleted successfully."));
    }
}
