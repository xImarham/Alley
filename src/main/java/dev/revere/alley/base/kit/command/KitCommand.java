package dev.revere.alley.base.kit.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.kit.command.impl.data.*;
import dev.revere.alley.base.kit.command.impl.data.inventory.KitGetInvCommand;
import dev.revere.alley.base.kit.command.impl.data.inventory.KitSetInvCommand;
import dev.revere.alley.base.kit.command.impl.data.potion.KitAddPotionCommand;
import dev.revere.alley.base.kit.command.impl.data.potion.KitClearPotionsCommand;
import dev.revere.alley.base.kit.command.impl.data.potion.KitRemovePotionCommand;
import dev.revere.alley.base.kit.command.impl.manage.*;
import dev.revere.alley.base.kit.command.impl.manage.ffa.KitSetFfaSlotCommand;
import dev.revere.alley.base.kit.command.impl.manage.ffa.KitSetupFFACommand;
import dev.revere.alley.base.kit.command.impl.manage.ffa.KitToggleFFACommand;
import dev.revere.alley.base.kit.command.impl.manage.raiding.KitSetRaidingRoleKitCommand;
import dev.revere.alley.base.kit.command.impl.settings.KitSetSettingCommand;
import dev.revere.alley.base.kit.command.impl.settings.KitSettingsCommand;
import dev.revere.alley.base.kit.command.impl.settings.KitViewSettingsCommand;
import dev.revere.alley.base.kit.command.impl.storage.KitSaveAllCommand;
import dev.revere.alley.base.kit.command.impl.storage.KitSaveCommand;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 21:58
 */
public class KitCommand extends BaseCommand {

    public KitCommand() {
        new KitSaveCommand();
        new KitSaveAllCommand();
        new KitCreateCommand();
        new KitDeleteCommand();
        new KitListCommand();
        new KitGetInvCommand();
        new KitSetInvCommand();
        new KitSetDescriptionCommand();
        new KitSetDisclaimerCommand();
        new KitSetDisplayNameCommand();
        new KitSetSettingCommand();
        new KitSettingsCommand();
        new KitSetIconCommand();
        new KitAddPotionCommand();
        new KitViewCommand();
        new KitViewSettingsCommand();
        new KitSetupFFACommand();
        new KitToggleFFACommand();
        new KitSetFfaSlotCommand();
        new KitSetCategoryCommand();
        new KitSetEditableCommand();
        new KitToggleCommand();
        new KitClearPotionsCommand();
        new KitRemovePotionCommand();
        new KitSetRaidingRoleKitCommand();
        new KitSetMenuTitleCommand();
    }

    @SuppressWarnings("unused")
    @CompleterData(name = "kit")
    public List<String> kitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            completion.addAll(Arrays.asList(
                    "list", "create", "delete", "toggle", "view", "settings", "viewsettings",
                    "setsetting", "setcategory", "setdescription", "setdisclaimer", "setdisplayname",
                    "seteditable", "seticon", "setinv", "getinv", "addpotion", "clearpotions", "removepotion",
                    "setupffa", "toggleffa", "setffaslot", "saveall", "save", "setraidingrolekit", "setmenutitle"
            ));
        }

        return completion;
    }

    @CommandData(name = "kit", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(CC.translate("&cInvalid page number."));
            }
        }

        if (page > this.pages.length || page < 1) {
            sender.sendMessage(CC.translate("&cNo more pages available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&b&lKit Commands &8(&7Page &f" + page + "&7/&f" + this.pages.length + "&8)"));
        for (String string : this.pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }
        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, this.pages.length, "/kit", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &f● &b/kit list &7| View all kits",
                    " &f● &b/kit create &8(&7kitName&8) &7| Create a kit",
                    " &f● &b/kit delete &8(&7kitName&8) &7| Delete a kit",
                    " &f● &b/kit toggle &8(&7kitName&8) &7| Toggle a kit",
                    " &f● &b/kit view &8(&7kitName&8) &7| View a kit",
            },
            {
                    " &f● &b/kit settings &7| View all existing kit settings",
                    " &f● &b/kit viewsettings &8(&7kitName&8) &7| View settings of a kit",
                    " &f● &b/kit setsetting &8(&7kitName&8) &8(&7setting&8) &8(&7enabled&8) &7| Set kit setting",
            },
            {
                    " &f● &b/kit setcategory &8(&7kitName&8) &8(&7category&8) &7| Set category of a kit",
                    " &f● &b/kit setdescription &8(&7kitName&8) &8(&7description&8) &7| Set description of a kit",
                    " &f● &b/kit setdisclaimer &8(&7kitName&8) &8(&7disclaimer&8) &7| Set disclaimer",
                    " &f● &b/kit setdisplayname &8(&7kitName&8) &8(&7displayname&8) &7| Set display-name of a kit",
                    " &f● &b/kit setmenutitle &8(&7kitName&8) &8(&7title&8) &7| Set menu title of a kit",
                    " &f● &b/kit seteditable &8(&7kitName&8) &8(&7true/false&8) &7| Set if a kit is editable",
                    " &f● &b/kit seticon &8(&7kitName&8) &7| Set icon of a kit",
            },
            {
                    " &f● &b/kit setinv &8(&7kitName&8) &7| Set inventory of a kit",
                    " &f● &b/kit getinv &8(&7kitName&8) &7| Get inventory of a kit",
            },
            {
                    " &f● &b/kit addpotion &8(&7kitName&8) &7| Set potion effects of a kit",
                    " &f● &b/kit removepotion &8(&7kitName&8) &7| Remove potion effects of a kit",
                    " &f● &b/kit clearpotions &8(&7kitName&8) &7| Clear potion effects of a kit",
            },
            {
                    " &f● &b/kit setupffa &8(&7kitName&8) &7| Setup ffa kit",
                    " &f● &b/kit toggleffa &8(&7kitName&8) &7| Toggle ffa kit",
                    " &f● &b/kit setffaslot &8(&7kitName&8) &8(&7slot&8) &7| Set ffa menu slot"
            },
            {
                    " &f● &b/kit setraidingrolekit &8(&7kitName&8) &8(&7role&8) &8(&7roleKitName&8) &7| Set raiding role kit",
            },
            {
                    " &f● &b/kit saveall &7| Save all kits",
                    " &f● &b/kit save &8(&7kitName&8) &7| Save a kit",
            }
    };
}