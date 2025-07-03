package dev.revere.alley.base.arena.command.impl.kit;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaRemoveKitCommand extends BaseCommand {

    @CompleterData(name = "arena.removekit")
    public List<String> arenaRemoveKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.removekit", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena removekit &6<arenaName> <kitName>"));
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getName() == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getService(IKitService.class).getKit(kitName).getName() == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (!Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().contains(kitName)) {
            player.sendMessage(CC.translate("&cThis arena does not have this kit!"));
            return;
        }

        player.sendMessage(CC.translate("&aKit &6" + kitName + "&a has been removed from arena &6" + arenaName + "&a!"));
        Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().remove(kitName);
        Alley.getInstance().getService(IArenaService.class).saveArena(Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName));
    }
}
