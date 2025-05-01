package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.enums.EnumKitCategory;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 01/05/2025
 */
public class KitSetCategoryCommand extends BaseCommand {
    @CommandData(name = "kit.setcategory", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit description &b<kitName> <description>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        EnumKitCategory category;

        try {
            category = EnumKitCategory.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid category. Available categories: " + Arrays.toString(EnumKitCategory.values())));
            return;
        }

        kit.setCategory(category);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(KitLocale.KIT_SET_CATEGORY.getMessage().replace("{kit-name}", kit.getName()).replace("{category}", category.getName())));
    }
}
