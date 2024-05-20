package me.emmy.alley.commands.admin.arena.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.arena.impl.FreeForAllArena;
import me.emmy.alley.arena.impl.SharedArena;
import me.emmy.alley.arena.impl.StandAloneArena;
import me.emmy.alley.arena.selection.Selection;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaCreateCommand extends BaseCommand {

    @Command(name = "arena.create", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage("Usage: /arena create <arenaName> <type>");
            return;
        }

        String arenaName = args[0];
        ArenaType arenaType = Arrays.stream(ArenaType.values())
                .filter(type -> type.name().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);

        if (arenaType == null) {
            player.sendMessage("Invalid arena type.");
            return;
        }

        if (Alley.getInstance().getArenaManager().getArenaByName(arenaName) != null) {
            player.sendMessage("An arena with that name already exists.");
            return;
        }

        Selection selection = Selection.createSelection(player);
        if (!selection.hasSelection()) {
            player.sendMessage("You must select the minimum and maximum locations for the arena.");
            return;
        }

        Arena arena;
        switch (arenaType) {
            case SHARED:
                arena = new SharedArena(arenaName, selection.getMinimum(), selection.getMaximum());
                break;
            case STANDALONE:
                arena = new StandAloneArena(arenaName, selection.getMinimum(), selection.getMaximum());
                break;
            case FFA:
                arena = new FreeForAllArena(arenaName, selection.getMinimum(), selection.getMaximum());
                break;
            default:
                return;
        }

        Alley.getInstance().getArenaManager().getArenas().add(arena);
    }
}
