package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.api.command.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 00:08
 */
public class ArenaTeleportCommand extends BaseCommand {

    @Completer(name = "arena.teleport", aliases = "arena.tp")
    public List<String> arenaTeleportCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Override
    @Command(name = "arena.teleport", aliases = "arena.tp", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena teleport (arenaName)"));
            return;
        }

        String arenaName = args[0];
        Arena arena = Alley.getInstance().getArenaRepository().getArenaByName(arenaName);

        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
            return;
        }

        player.teleport(arena.getCenter());

    }
}
