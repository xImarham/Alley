package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSetSpawnCommand extends BaseCommand {
    @Command(name = "arena.setspawn", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setspawn <arenaName> <a/b>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("a") && !spawnType.equalsIgnoreCase("b")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: A, B"));
            return;
        }

        if (spawnType.equalsIgnoreCase("a")) {
            Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setPos1(player.getLocation());
            player.sendMessage(CC.translate("&aSpawn A has been set for arena &b" + arenaName + "&a!"));
        } else {
            Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setPos2(player.getLocation());
            player.sendMessage(CC.translate("&aSpawn B has been set for arena &b" + arenaName + "&a!"));
        }

        Alley.getInstance().getArenaRepository().saveArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
    }
}
