package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.api.command.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaKitListCommand extends BaseCommand {

    @Completer(name = "arena.kitlist")
    public List<String> arenaKitListCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Command(name = "arena.kitlist", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena kitlist <arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&l" + arenaName + " Kit List &f(" + Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().size() + "&f)"));
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Arena Kits available."));
        }
        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().forEach(kit -> player.sendMessage(CC.translate("      &f● &b" + kit)));
        player.sendMessage("");
    }
}
