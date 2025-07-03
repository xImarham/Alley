package dev.revere.alley.base.arena.command.impl.kit;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaKitListCommand extends BaseCommand {

    @CompleterData(name = "arena.kitlist")
    public List<String> arenaKitListCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.kitlist", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena kitlist &6<arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &6&l" + arenaName + " Kit List &f(" + Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().size() + "&f)"));
        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Arena Kits available."));
        }
        Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().forEach(kit -> player.sendMessage(CC.translate("      &f● &6" + kit)));
        player.sendMessage("");
    }
}
