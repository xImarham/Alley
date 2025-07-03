package dev.revere.alley.base.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaListCommand extends BaseCommand {
    @CommandData(name = "arena.list", aliases = {"arenas"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        IArenaService arenaService = Alley.getInstance().getService(IArenaService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &6&lArena List &f(" + arenaService.getArenas().size() + "&f)"));
        if (arenaService.getArenas().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Arenas available."));
        }

        arenaService.getArenas().stream().filter(arena -> arena.getType() != EnumArenaType.FFA).forEach(arena ->
                player.sendMessage(CC.translate("      &f● &6" + arena.getName() + " &7(" + arena.getType().name() + ")" + (arena.isEnabled() ? " &aEnabled" : " &cDisabled"))))
        ;
        arenaService.getArenas().stream().filter(arena -> arena.getType() == EnumArenaType.FFA).forEach(arena ->
                player.sendMessage(CC.translate("      &f● &6" + arena.getName() + " &7(" + arena.getType().name() + ")")))
        ;

        player.sendMessage("");
    }
}
