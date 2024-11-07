package dev.revere.alley.arena.command.impl.storage;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSaveAllCommand extends BaseCommand {
    @Command(name = "arena.saveall", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (Arena arena : Alley.getInstance().getArenaRepository().getArenas()) {
            Alley.getInstance().getArenaRepository().saveArena(arena);
        }

        player.sendMessage(CC.translate("&aAll arenas have been saved!"));
    }
}
