package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSaveCommand extends BaseCommand {
    @Command(name = "arena.save", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (Arena arena : Alley.getInstance().getArenaRepository().getArenas()) {
            Alley.getInstance().getArenaRepository().saveArena(arena);
        }

        player.sendMessage(CC.translate("&aAll arenas have been saved!"));
    }
}
