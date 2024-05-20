package me.emmy.alley.commands.admin.arena;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 02/05/2024 - 19:02
 */

public class ArenaCommand extends BaseCommand {
    @Override
    @Command(name = "arena", permission = "practice.owner")
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(CC.translate("&b&lArena Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &b/arena create &8(&7arenaName&8) &7| Create an arena"));
        sender.sendMessage(CC.translate(" &f● &b/arena delete &8(&7arenaName&8) &7| Delete an arena"));
        sender.sendMessage(CC.translate(" &f● &b/arena list &7| List all arenas"));
        sender.sendMessage(CC.translate(" &f● &b/arena setpos1 &8(&7arenaName&8) &7| Set 1st arena position"));
        sender.sendMessage(CC.translate(" &f● &b/arena setpos2 &8(&7arenaName&8) &7| Set 2nd arena position"));
        sender.sendMessage(CC.translate(" &f● &b/arena setmaximum &8(&7arenaName&8) &7| Set maximum edge"));
        sender.sendMessage(CC.translate(" &f● &b/arena setminimum &8(&7arenaName&8) &7| Set minimum edge"));
        sender.sendMessage(CC.translate(" &f● &b/arena setcenter &8(&7arenaName&8) &7| Set 2nd arena position"));
        sender.sendMessage(CC.translate(" &f● &b/arena setenabled &8(&7arenaName&8) &8<&7true/false&8> &7| Enable or Disable an Arena"));
        sender.sendMessage(CC.translate(" &f● &b/arena save &7| Save all arenas"));
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage("");
    }
}
