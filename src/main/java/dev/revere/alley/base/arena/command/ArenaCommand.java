package dev.revere.alley.base.arena.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.command.impl.data.*;
import dev.revere.alley.base.arena.command.impl.kit.ArenaAddKitCommand;
import dev.revere.alley.base.arena.command.impl.kit.ArenaKitListCommand;
import dev.revere.alley.base.arena.command.impl.kit.ArenaRemoveKitCommand;
import dev.revere.alley.base.arena.command.impl.manage.*;
import dev.revere.alley.base.arena.command.impl.storage.ArenaSaveAllCommand;
import dev.revere.alley.base.arena.schematic.command.ArenaPasteTestCommand;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 02/05/2024 - 19:02
 */
public class ArenaCommand extends BaseCommand {

    /**
     * Register all Arena subcommands in the constructor
     */
    public ArenaCommand() {
        new ArenaSetHeightLimit();
        new ArenaSetPortalCommand();
        new ArenaSetCenterCommand();
        new ArenaCreateCommand();
        new ArenaSetCuboidCommand();
        new ArenaTeleportCommand();
        new ArenaDeleteCommand();
        new ArenaAddKitCommand();
        new ArenaKitListCommand();
        new ArenaListCommand();
        new ArenaRemoveKitCommand();
        new ArenaSaveAllCommand();
        new ArenaSetSpawnCommand();
        new ArenaToggleCommand();
        new ArenaToolCommand();
        new ArenaSetDisplayNameCommand();
        new ArenaViewCommand();
        new ArenaTestCommand();
        new ArenaPasteTestCommand();
    }

    @SuppressWarnings("unused")
    @CompleterData(name = "arena")
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
            completion.add("saveall");
            completion.add("setsafezone");
            completion.add("setdisplayname");
        }

        return completion;
    }

    @CommandData(name = "arena", isAdminOnly = true, inGameOnly = false)
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

        if (page > pages.length || page < 1) {
            sender.sendMessage(CC.translate("&cNo more pages available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&b&lArena Commands &8(&7Page &f" + page + "&7/&f" + pages.length + "&8)"));
        for (String string : pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }
        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, pages.length, "/arena", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &f● &b/arena list &7| List all arenas",
                    " &f● &b/arena kitlist &8(&7arenaName&8) &7| List all kits for an arena",
                    " &f● &b/arena saveall &7| Save all arenas",
                    " &f● &b/arena view &8(&7arenaName&8) &7| View arena information",
                    " &f● &b/arena delete &8(&7arenaName&8) &7| Delete an arena",
                    " &f● &b/arena create &8(&7arenaName&8) &7| Create an arena",
                    " &f● &b/arena tool &7| Get the Arena Selection tool"
            },
            {
                    " &f● &b/arena setdisplayname &8(&7arenaName&8) &8(&7displayname&8) &7| Set display-name of an arena",
                    " &f● &b/arena setcenter &8(&7arenaName&8) &7| Set center position",
                    " &f● &b/arena setcuboid &8(&7arenaName&8) &7| Set min and max position",
                    " &f● &b/arena setspawn &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set spawn positions",
                    " &f● &b/arena removekit &8(&7arenaName&8) &8(&7kitName&8) &7| Remove arena kit",
                    " &f● &b/arena teleport &8(&7arenaName&8) &7| Teleport to an arena",
                    " &f● &b/arena toggle &8(&7arenaName&8) &7| Enable or Disable an Arena",
                    " &f● &b/arena addkit &8(&7arenaName&8) &8(&7kitName&8) &7| Add a kit to an arena"
            },
            {
                    " &f● &b/arena setportal &8(&7arenaName&8) &8<&71/2&8> &7| Set portal positions for battlerush",
                    " &f● &b/arena setheightlimit &8(&7arenaName&8) &7| Set height limit for standalone arenas",
            }
    };
}