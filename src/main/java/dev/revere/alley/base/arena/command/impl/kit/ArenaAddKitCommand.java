package dev.revere.alley.base.arena.command.impl.kit;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.config.locale.impl.ArenaLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaAddKitCommand extends BaseCommand {

    @CompleterData(name = "arena.addkit")
    public List<String> arenaAddKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.addkit", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena addkit &6<arenaName> <kitName>"));
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName) == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou cannot add kits to Free-For-All arenas!"));
            return;
        }

        if (Alley.getInstance().getService(IKitService.class).getKit(kitName) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().contains(kitName)) {
            player.sendMessage(CC.translate("&cThis arena already has this kit!"));
            return;
        }

        player.sendMessage(CC.translate("&aKit &6" + kitName + "&a has been added to arena &6" + arenaName + "&a!"));
        Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().add(Alley.getInstance().getService(IKitService.class).getKit(kitName).getName());
        Alley.getInstance().getService(IArenaService.class).saveArena(Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName));
    }
}
