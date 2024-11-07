package dev.revere.alley.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.ArenaType;
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
public class ArenaListCommand extends BaseCommand {
    @Command(name = "arena.list", aliases = {"arenas"},  permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lArena List &f(" + Alley.getInstance().getArenaRepository().getArenas().size() + "&f)"));
        if (Alley.getInstance().getArenaRepository().getArenas().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Arenas available."));
        }

        Alley.getInstance().getArenaRepository().getArenas().stream().filter(arena -> arena.getType() != ArenaType.FFA).forEach(arena ->
                player.sendMessage(CC.translate("      &f● &b" + arena.getName() + " &7(" + arena.getType().name() + ")" + (arena.isEnabled() ? " &aEnabled" : " &cDisabled"))))
        ;
        Alley.getInstance().getArenaRepository().getArenas().stream().filter(arena -> arena.getType() == ArenaType.FFA).forEach(arena ->
                player.sendMessage(CC.translate("      &f● &b" + arena.getName() + " &7(" + arena.getType().name() + ")")))
        ;

        player.sendMessage("");
    }
}
