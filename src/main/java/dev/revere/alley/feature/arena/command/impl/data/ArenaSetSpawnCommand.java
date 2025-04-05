package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSetSpawnCommand extends BaseCommand {

    @CompleterData(name = "arena.setspawn")
    public List<String> arenaSetSpawnCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            this.plugin.getArenaService().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.setspawn", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setspawn &b<arenaName> <pos1/pos2/ffa>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        if (this.plugin.getArenaService().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("pos1") && !spawnType.equalsIgnoreCase("pos2") && !spawnType.equalsIgnoreCase("ffa")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: pos1, pos2, ffa"));
            return;
        }

        switch (spawnType.toLowerCase()) {
            case "pos1":
                if (this.plugin.getArenaService().getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                this.plugin.getArenaService().getArenaByName(arenaName).setPos1(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position 1 has been set for arena &b" + arenaName + "&a!"));
                break;
            case "ffa":
                if (this.plugin.getArenaService().getArenaByName(arenaName).getType() != EnumArenaType.FFA) {
                    player.sendMessage(CC.translate("&cThis arena is not an FFA arena!"));
                    return;
                }
                this.plugin.getArenaService().getArenaByName(arenaName).setPos1(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position has been set for arena &b" + arenaName + "&a!"));
                break;
            default:
                if (this.plugin.getArenaService().getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                this.plugin.getArenaService().getArenaByName(arenaName).setPos2(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position 2 has been set for arena &b" + arenaName + "&a!"));
                break;
        }

        this.plugin.getArenaService().saveArena(this.plugin.getArenaService().getArenaByName(arenaName));
    }
}
