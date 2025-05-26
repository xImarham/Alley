package dev.revere.alley.base.arena.command.impl.kit;

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
public class ArenaRemoveKitCommand extends BaseCommand {

    @CompleterData(name = "arena.removekit")
    public List<String> arenaRemoveKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            this.plugin.getArenaService().getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.removekit", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena removekit &b<arenaName> <kitName>"));
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        if (this.plugin.getArenaService().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (this.plugin.getKitService().getKit(kitName) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (!this.plugin.getArenaService().getArenaByName(arenaName).getKits().contains(kitName)) {
            player.sendMessage(CC.translate("&cThis arena does not have this kit!"));
            return;
        }

        player.sendMessage(CC.translate("&aKit &b" + kitName + "&a has been removed from arena &b" + arenaName + "&a!"));
        this.plugin.getArenaService().getArenaByName(arenaName).getKits().remove(kitName);
        this.plugin.getArenaService().saveArena(this.plugin.getArenaService().getArenaByName(arenaName));
    }
}
