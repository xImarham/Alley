package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import me.emmy.alley.utils.command.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSetSpawnCommand extends BaseCommand {

    @Completer(name = "arena.setspawn")
    public List<String> arenaSetSpawnCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Command(name = "arena.setspawn", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setspawn <arenaName> <pos1/pos2/ffa>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("pos1") && !spawnType.equalsIgnoreCase("pos2") && !spawnType.equalsIgnoreCase("ffa")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: pos1, pos2, ffa"));
            return;
        }

        switch (spawnType.toLowerCase()) {
            case "pos1":
                if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getType() == ArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setPos1(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position 1 has been set for arena &b" + arenaName + "&a!"));
                break;
            case "ffa":
                if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getType() != ArenaType.FFA) {
                    player.sendMessage(CC.translate("&cThis arena is not an FFA arena!"));
                    return;
                }
                Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setPos1(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position has been set for arena &b" + arenaName + "&a!"));
                break;
            default:
                if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getType() == ArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setPos2(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position 2 has been set for arena &b" + arenaName + "&a!"));
                break;
        }

        Alley.getInstance().getArenaRepository().saveArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
    }
}
