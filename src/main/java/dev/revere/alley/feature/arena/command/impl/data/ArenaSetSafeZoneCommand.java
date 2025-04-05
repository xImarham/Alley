package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 21:56
 */
public class ArenaSetSafeZoneCommand extends BaseCommand {
    @Override
    @CommandData(name = "arena.setsafezone", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setsafezone &b<arenaName> <pos1/pos2>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        if (this.plugin.getArenaService().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (this.plugin.getArenaService().getArenaByName(arenaName).getType() != EnumArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou can only set the safezone for Free-For-All arenas!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("pos1") && !spawnType.equalsIgnoreCase("pos2")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: pos1, pos2"));
            return;
        }

        if (spawnType.equalsIgnoreCase("pos1")) {
            this.plugin.getArenaService().getArenaByName(arenaName).setMaximum(player.getLocation());
            player.sendMessage(CC.translate("&aSafe Zone position 1 has been set for arena &b" + arenaName + "&a!"));
        } else {
            this.plugin.getArenaService().getArenaByName(arenaName).setMinimum(player.getLocation());
            player.sendMessage(CC.translate("&aSafe Zone position 2 has been set for arena &b" + arenaName + "&a!"));
        }

        this.plugin.getArenaService().saveArena(this.plugin.getArenaService().getArenaByName(arenaName));
    }
}
