package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSetCenterCommand extends BaseCommand {

    @CompleterData(name = "arena.setcenter")
    public List<String> arenaSetCenterCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getArenaService().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }


    @CommandData(name = "arena.setcenter", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setcenter &b<arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getArenaService().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        Alley.getInstance().getArenaService().getArenaByName(arenaName).setCenter(player.getLocation());
        Alley.getInstance().getArenaService().saveArena(Alley.getInstance().getArenaService().getArenaByName(arenaName));
        player.sendMessage(CC.translate("&aCenter has been set for arena &b" + arenaName + "&a!"));
    }
}
