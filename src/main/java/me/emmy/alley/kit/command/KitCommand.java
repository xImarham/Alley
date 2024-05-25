package me.emmy.alley.kit.command;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 21:58
 */

public class KitCommand extends BaseCommand {
    @Override
    @Command(name = "kit", permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(CC.translate("&d&lKit Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &d/kit create &8(&7kitName&8) &7| Create a kit"));
        sender.sendMessage(CC.translate(" &f● &d/kit delete &8(&7kitName&8) &7| Delete a kit"));
        sender.sendMessage(CC.translate(" &f● &d/kit getinv &8(&7kitName&8) &7| Get inventory of a kit"));
        sender.sendMessage(CC.translate(" &f● &d/kit list &7| View all kits"));
        sender.sendMessage(CC.translate(" &f● &d/kit saveall &7| Save all kits"));
        sender.sendMessage(CC.translate(" &f● &d/kit save &8(&7kitName&8) &7| Save a specific kit"));
        sender.sendMessage(CC.translate(" &f● &d/kit setdesc &8(&7kitName&8) &8(&7description&8) &7| Set desc of a kit"));
        sender.sendMessage(CC.translate(" &f● &d/kit seteditorslot &8(&7kitName&8) &8(&7slot&8) &7| Set editor menu slot"));
        sender.sendMessage(CC.translate(" &f● &d/kit setinv &8(&7kitName&8) &7| Set inventory of a kit"));
        sender.sendMessage(CC.translate(" &f● &d/kit setrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set ranked menu slot"));
        sender.sendMessage(CC.translate(" &f● &d/kit setunrankedslot &8(&7kitName&8) &8(&7slot&8) &7| Set unranked menu slot"));
        //sender.sendMessage(CC.translate(" &f● &d/kit setdisplayname &8(&7kitName&8) &7| Set display-name of a kit"));
        //sender.sendMessage(CC.translate(" &f● &d/kit seticon &8(&7kitName&8) &7| Set icon of a kit"));
        //sender.sendMessage(CC.translate(" &f● &d/kit addarena &8(&7kitName&8) &8(&7arena&8) &7| Add an arena to a kit"));
        //sender.sendMessage(CC.translate(" &f● &d/kit setrule &8(&7rule&8) &8(&7value&8) &7| Toggle kit rules"));
        //sender.sendMessage(CC.translate(" &f● &d/kit viewrules &8(&7kitName&8) &7| View rules of a kit"));
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage("");
    }
}
