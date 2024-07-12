package me.emmy.alley.arena.command;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import me.emmy.alley.utils.command.Completer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Practice
 * @date 02/05/2024 - 19:02
 */
public class ArenaCommand extends BaseCommand {

    @Completer(name = "arena", aliases = "arena")
    public List<String> arenaCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            completion.add("create");
            completion.add("delete");
            completion.add("list");
            completion.add("kitlist");
            completion.add("setcuboid");
            completion.add("setcenter");
            completion.add("setspawn");
            completion.add("removekit");
            completion.add("addkit");
            completion.add("teleport");
            completion.add("toggle");
            completion.add("tool");
            completion.add("save");
            completion.add("setsafezone");
        }

        return completion;
    }

    @Override
    @Command(name = "arena", permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        CommandSender player = args.getPlayer();

        player.sendMessage(" ");
        player.sendMessage(CC.translate("&d&lArena Commands Help:"));
        player.sendMessage(CC.translate(" &f● &d/arena create &8(&7arenaName&8) &7| Create an arena"));
        player.sendMessage(CC.translate(" &f● &d/arena delete &8(&7arenaName&8) &7| Delete an arena"));
        player.sendMessage(CC.translate(" &f● &d/arena list &7| List all arenas"));
        player.sendMessage(CC.translate(" &f● &d/arena kitlist &7| List all kits for an arena"));
        player.sendMessage(CC.translate(" &f● &d/arena setcenter &8(&7arenaName&8) &7| Set center position"));
        player.sendMessage(CC.translate(" &f● &d/arena setcuboid &8(&7arenaName&8) &7| Set min and max position"));
        player.sendMessage(CC.translate(" &f● &d/arena setspawn &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set spawn positions"));
        player.sendMessage(CC.translate(" &f● &d/arena removekit &8(&7arenaName&8) &8(&7kitName&8) &7| Remove arena kit"));
        player.sendMessage(CC.translate(" &f● &d/arena addkit &8(&7arenaName&8) &8(&7kitName&8) &7| Add a kit to an arena"));
        player.sendMessage(CC.translate(" &f● &d/arena teleport &8(&7arenaName&8) &7| Teleport to an arena"));
        player.sendMessage(CC.translate(" &f● &d/arena toggle &8(&7arenaName&8) &7| Enable or Disable an Arena"));
        player.sendMessage(CC.translate(" &f● &d/arena tool &7| Get the Arena Selection tool"));
        player.sendMessage(CC.translate(" &f● &d/arena save &7| Save all arenas"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&d&lFFA Arena Help:"));
        player.sendMessage(CC.translate(" &f● &d/arena setsafezone &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set safezone positions"));
        player.sendMessage("");
    }
}
