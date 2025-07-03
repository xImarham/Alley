package dev.revere.alley.base.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.arena.selection.ArenaSelection;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSetCuboidCommand extends BaseCommand {

    @CompleterData(name = "arena.setcuboid")
    public List<String> arenaCuboidCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.setcuboid", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setcuboid &6<arenaName>"));
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(CC.translate("&cYou must select the minimum and maximum locations for the arena."));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou cannot set cuboids for Free-For-All arenas! You must use: &4/arena setsafezone pos1/pos2&c."));
            return;
        }

        Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).setMinimum(arenaSelection.getMinimum());
        Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).setMaximum(arenaSelection.getMaximum());
        player.sendMessage(CC.translate("&aCuboid has been set for arena &6" + arenaName + "&a!"));

        Alley.getInstance().getService(IArenaService.class).saveArena(Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName));
    }
}
