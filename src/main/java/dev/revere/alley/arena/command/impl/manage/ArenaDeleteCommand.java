package dev.revere.alley.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.Completer;
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
            player.sendMessage(CC.translate("&6Usage: &e/arena delete &b<arenaName>"));
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
