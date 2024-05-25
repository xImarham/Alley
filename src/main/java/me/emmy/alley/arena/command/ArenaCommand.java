package me.emmy.alley.arena.command;

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
    @Command(name = "arena", permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(CC.translate("&d&lArena Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &d/arena create &8(&7arenaName&8) &7| Create an arena"));
        sender.sendMessage(CC.translate(" &f● &d/arena delete &8(&7arenaName&8) &7| Delete an arena"));
        sender.sendMessage(CC.translate(" &f● &d/arena list &7| List all arenas"));
        sender.sendMessage(CC.translate(" &f● &d/arena kitlist &7| List all kits for an arena"));
        sender.sendMessage(CC.translate(" &f● &d/arena setcuboid &8(&7arenaName&8) &7| Set min and max position"));
        sender.sendMessage(CC.translate(" &f● &d/arena setcenter &8(&7arenaName&8) &7| Set center position"));
        sender.sendMessage(CC.translate(" &f● &d/arena setspawn &8(&7arenaName&8) &8<&7a/b&8> &7| Set spawn positions"));
        sender.sendMessage(CC.translate(" &f● &d/arena removekit &8(&7arenaName&8) &8(&7kitName&8) &7| Remove a kit from an arena"));
        sender.sendMessage(CC.translate(" &f● &d/arena addkit &8(&7arenaName&8) &8(&7kitName&8) &7| Add a kit to an arena"));
        sender.sendMessage(CC.translate(" &f● &d/arena toggle &8(&7arenaName&8) &7| Enable or Disable an Arena"));
        sender.sendMessage(CC.translate(" &f● &d/arena tool &7| Get the Arena Selection tool"));
        sender.sendMessage(CC.translate(" &f● &d/arena save &7| Save all arenas"));
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage("");
    }
}
