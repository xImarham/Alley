package me.emmy.alley.kit.command;

import me.emmy.alley.kit.command.impl.data.KitSetDescriptionCommand;
import me.emmy.alley.kit.command.impl.data.KitSetDisplayNameCommand;
import me.emmy.alley.kit.command.impl.data.KitSetIconCommand;
import me.emmy.alley.kit.command.impl.data.inventory.KitGetInvCommand;
import me.emmy.alley.kit.command.impl.data.inventory.KitSetInvCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetEditorSlotCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetRankedSlotCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetUnrankedSlotCommand;
import me.emmy.alley.kit.command.impl.manage.KitCreateCommand;
import me.emmy.alley.kit.command.impl.manage.KitDeleteCommand;
import me.emmy.alley.kit.command.impl.manage.KitListCommand;
import me.emmy.alley.kit.command.impl.manage.KitViewCommand;
import me.emmy.alley.kit.command.impl.settings.KitSetSettingCommand;
import me.emmy.alley.kit.command.impl.settings.KitSettingsCommand;
import me.emmy.alley.kit.command.impl.storage.KitSaveAllCommand;
import me.emmy.alley.kit.command.impl.storage.KitSaveCommand;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Practice
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
        new KitSetDisplayNameCommand();
        new KitSetEditorSlotCommand();
        new KitSetUnrankedSlotCommand();
        new KitSetRankedSlotCommand();
        new KitSetSettingCommand();
        new KitSettingsCommand();
        new KitSetIconCommand();
        new KitViewCommand();
    }
    @Override
    @Command(name = "kit", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&b&lKit Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &b/kit list &7| View all kits"));
        sender.sendMessage(CC.translate(" &f● &b/kit saveall &7| Save all kits"));
        sender.sendMessage(CC.translate(" &f● &b/kit settings &7| View all kit settings"));
        sender.sendMessage(CC.translate(" &f● &b/kit save &8(&7kitName&8) &7| Save a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit view &8(&7kitName&8) &7| View a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit delete &8(&7kitName&8) &7| Delete a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit create &8(&7kitName&8) &7| Create a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit seticon &8(&7kitName&8) &7| Set icon of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit setinv &8(&7kitName&8) &7| Set inventory of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit getinv &8(&7kitName&8) &7| Get inventory of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit setdesc &8(&7kitName&8) &8(&7description&8) &7| Set desc of a kit"));
        sender.sendMessage(CC.translate(" &f● &b/kit seteditorslot &8(&7kitName&8) &8(&7slot&8) &7| Set editor menu slot"));
        sender.sendMessage(CC.translate(" &f● &b/kit setrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set ranked menu slot"));
        sender.sendMessage(CC.translate(" &f● &b/kit setunrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set unranked menu slot"));
        sender.sendMessage(CC.translate(" &f● &b/kit setsetting &8(&7kitName&8) &8(&7setting&8) &8(&7enabled&8) &7| Set kit setting"));
        sender.sendMessage(CC.translate(" &f● &b/kit setdisplayname &8(&7kitName&8) &8(&7displayname&8) &7| Set display-name of a kit"));
        sender.sendMessage("");
    }
}
