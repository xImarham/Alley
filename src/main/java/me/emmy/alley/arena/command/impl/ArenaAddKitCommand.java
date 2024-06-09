package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
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
public class ArenaAddKitCommand extends BaseCommand {

    @Completer(name = "arena.addkit")
    public List<String> arenaAddKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Command(name = "arena.addkit", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena addkit <arenaName> <kitName>"));
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getKitRepository().getKit(kitName) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().contains(kitName)) {
            player.sendMessage(CC.translate("&cThis arena already has this kit!"));
            return;
        }

        player.sendMessage(CC.translate("&aKit &b" + kitName + "&a has been added to arena &b" + arenaName + "&a!"));
        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().add(Alley.getInstance().getKitRepository().getKit(kitName).getName());
        Alley.getInstance().getArenaRepository().saveArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
    }
}
