package dev.revere.alley.base.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
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
public class ArenaToggleCommand extends BaseCommand {

    @CompleterData(name = "arena.toggle")
    public List<String> arenaToggleCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getService(IArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.toggle", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena toggle &6<arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName) == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getType() == EnumArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou cannot enable or disable Free-For-All arenas!"));
            return;
        }

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getMinimum() == null || Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getMaximum() == null || Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getPos1() == null || Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getPos2() == null) {
            player.sendMessage(CC.translate("&cYou must finish configuring this arena before enabling or disabling!"));
            return;
        }

        if (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).getKits().isEmpty()) {
            player.sendMessage(CC.translate("&cYou must add at least one kit to this arena before enabling or disabling!"));
            return;
        }

        Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).setEnabled(!Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).isEnabled());
        Alley.getInstance().getService(IArenaService.class).saveArena(Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName));
        player.sendMessage(CC.translate("&aArena &6" + arenaName + "&a has been " + (Alley.getInstance().getService(IArenaService.class).getArenaByName(arenaName).isEnabled() ? "enabled" : "disabled") + "&a!"));
    }
}
