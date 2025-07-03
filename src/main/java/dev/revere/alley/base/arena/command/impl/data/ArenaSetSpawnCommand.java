package dev.revere.alley.base.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.util.chat.CC;
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
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.setspawn", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setspawn &6<arenaName> <blue/red/ffa>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("blue") && !spawnType.equalsIgnoreCase("red") && !spawnType.equalsIgnoreCase("ffa")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: blue, red, ffa"));
            return;
        }

        switch (spawnType.toLowerCase()) {
            case "blue":
                if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).setPos1(player.getLocation());
                player.sendMessage(CC.translate("&aBlue Spawn Position has been set for arena &6" + arenaName + "&a!"));
                break;
            case "ffa":
                if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getType() != EnumArenaType.FFA) {
                    player.sendMessage(CC.translate("&cThis arena is not an FFA arena!"));
                    return;
                }
                Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).setPos1(player.getLocation());
                player.sendMessage(CC.translate("&aSpawn Position has been set for arena &6" + arenaName + "&a!"));
                break;
            default:
                if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).setPos2(player.getLocation());
                player.sendMessage(CC.translate("&aRed Spawn Position has been set for arena &6" + arenaName + "&a!"));
                break;
        }

        Alley.getInstance().getService(IArenaService.class).saveArena(Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName));
    }
}
