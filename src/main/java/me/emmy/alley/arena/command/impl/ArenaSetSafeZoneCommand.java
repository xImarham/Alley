package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 21:56
 */
public class ArenaSetSafeZoneCommand extends BaseCommand {
    @Override
    @Command(name = "arena.setsafezone", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setsafezone <arena> <pos1/pos2>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getType() != ArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou can only set the safezone for Free-For-All arenas!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("pos1") && !spawnType.equalsIgnoreCase("pos2")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: pos1, pos2"));
            return;
        }

        if (spawnType.equalsIgnoreCase("pos1")) {
            Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setMaximum(player.getLocation());
            player.sendMessage(CC.translate("&aSafe Zone position 1 has been set for arena &b" + arenaName + "&a!"));
        } else {
            Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setMinimum(player.getLocation());
            player.sendMessage(CC.translate("&aSafe Zone position 2 has been set for arena &b" + arenaName + "&a!"));
        }

        Alley.getInstance().getArenaRepository().saveArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
    }
}
