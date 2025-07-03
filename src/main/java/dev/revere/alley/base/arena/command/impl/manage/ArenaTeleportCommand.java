package dev.revere.alley.base.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 00:08
 */
public class ArenaTeleportCommand extends BaseCommand {

    @CompleterData(name = "arena.teleport", aliases = "arena.tp")
    public List<String> arenaTeleportCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Override
    @CommandData(name = "arena.teleport", aliases = "arena.tp", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena teleport &6<arenaName>"));
            return;
        }

        String arenaName = args[0];
        AbstractArena arena = Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName);

        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
            return;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena does not have a defined center location."));
            if (arena.getPos1() == null) {
                return;
            }

            player.teleport(arena.getPos1());
            player.sendMessage(CC.translate("&aYou have been teleported to position 1 instead."));
            return;
        }

        player.teleport(arena.getCenter());
    }
}