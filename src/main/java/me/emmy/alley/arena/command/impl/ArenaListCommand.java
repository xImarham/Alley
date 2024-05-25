package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaListCommand extends BaseCommand {
    @Command(name = "arena.list", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("     &d&lArena List &f(" + Alley.getInstance().getArenaRepository().getArenas().size() + "&f)"));
        if (Alley.getInstance().getArenaRepository().getArenas().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Arenas available."));
        }
        Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> player.sendMessage(CC.translate("      &f● &d" + arena.getName() + " &7(" + arena.getType().name() + ")" + (arena.isEnabled() ? " &aEnabled" : " &cDisabled"))));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");
    }
}
