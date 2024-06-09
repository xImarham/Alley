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
public class ArenaDeleteCommand extends BaseCommand {

    @Completer(name = "arena.delete")
    public List<String> arenaDeleteCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Command(name = "arena.delete", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena delete <arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        player.sendMessage(CC.translate("&aArena &b" + arenaName + "&a has been deleted!"));
        Alley.getInstance().getArenaRepository().deleteArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
    }
}
