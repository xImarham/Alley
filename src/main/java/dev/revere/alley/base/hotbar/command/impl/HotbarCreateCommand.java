package dev.revere.alley.base.hotbar.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.hotbar.enums.HotbarType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
public class HotbarCreateCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.create",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/hotbar create &6<name> <type>"));
            return;
        }

        String name = args[0];

        HotbarType type;
        try {
            type = HotbarType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid hotbar type! Valid types are: " + HotbarType.availableTypes()));
            return;
        }

        this.plugin.getService(HotbarService.class).createHotbarItem(name, type);
        player.sendMessage(CC.translate("&aHotbar item &e" + name + " &acreated successfully with type &e" + type.name() + "&a!"));
    }
}