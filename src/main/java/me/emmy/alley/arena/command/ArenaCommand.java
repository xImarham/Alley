package me.emmy.alley.arena.command;

import me.emmy.alley.arena.command.impl.*;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.api.command.Completer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Practice
 * @date 02/05/2024 - 19:02
 */
public class ArenaCommand extends BaseCommand {

    /**
     * Register all Arena subcommands in the constructor
     */
    public ArenaCommand() {
        new ArenaSetSafeZoneCommand();
        new ArenaSetCenterCommand();
        new ArenaCreateCommand();
        new ArenaSetCuboidCommand();
        new ArenaTeleportCommand();
        new ArenaDeleteCommand();
        new ArenaAddKitCommand();
        new ArenaKitListCommand();
        new ArenaListCommand();
        new ArenaRemoveKitCommand();
        new ArenaSaveCommand();
        new ArenaSetSpawnCommand();
        new ArenaToggleCommand();
        new ArenaToolCommand();
    }

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
        player.sendMessage(CC.translate("&b&lArena Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/arena create &8(&7arenaName&8) &7| Create an arena"));
        player.sendMessage(CC.translate(" &f● &b/arena delete &8(&7arenaName&8) &7| Delete an arena"));
        player.sendMessage(CC.translate(" &f● &b/arena list &7| List all arenas"));
        player.sendMessage(CC.translate(" &f● &b/arena kitlist &7| List all kits for an arena"));
        player.sendMessage(CC.translate(" &f● &b/arena setcenter &8(&7arenaName&8) &7| Set center position"));
        player.sendMessage(CC.translate(" &f● &b/arena setcuboid &8(&7arenaName&8) &7| Set min and max position"));
        player.sendMessage(CC.translate(" &f● &b/arena setspawn &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set spawn positions"));
        player.sendMessage(CC.translate(" &f● &b/arena removekit &8(&7arenaName&8) &8(&7kitName&8) &7| Remove arena kit"));
        player.sendMessage(CC.translate(" &f● &b/arena addkit &8(&7arenaName&8) &8(&7kitName&8) &7| Add a kit to an arena"));
        player.sendMessage(CC.translate(" &f● &b/arena teleport &8(&7arenaName&8) &7| Teleport to an arena"));
        player.sendMessage(CC.translate(" &f● &b/arena toggle &8(&7arenaName&8) &7| Enable or Disable an Arena"));
        player.sendMessage(CC.translate(" &f● &b/arena tool &7| Get the Arena Selection tool"));
        player.sendMessage(CC.translate(" &f● &b/arena save &7| Save all arenas"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lFFA Arena Help:"));
        player.sendMessage(CC.translate(" &f● &b/arena setsafezone &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set safezone positions"));
        player.sendMessage("");
    }
}
