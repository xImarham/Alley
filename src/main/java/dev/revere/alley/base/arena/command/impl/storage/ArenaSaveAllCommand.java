package dev.revere.alley.base.arena.command.impl.storage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.config.locale.impl.ArenaLocale;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSaveAllCommand extends BaseCommand {
    @CommandData(name = "arena.saveall", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (AbstractArena arena : this.plugin.getService(IArenaService.class).getArenas()) {
            this.plugin.getService(IArenaService.class).saveArena(arena);
        }

        player.sendMessage(ArenaLocale.SAVED_ALL.getMessage());
    }
}
