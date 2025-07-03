package dev.revere.alley.base.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/03/2025
 */
public class ArenaSetVoidLevelCommand extends BaseCommand {
    @CommandData(name = "arena.setvoidlevel", aliases = "arena.void", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setvoidlevel &6<arenaName> <voidLevel>"));
            return;
        }

        IArenaService arenaService = Alley.getInstance().getService(IArenaService.class);
        AbstractArena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arena.getType() != EnumArenaType.STANDALONE) {
            player.sendMessage(CC.translate("&cYou can only set the void level for standalone arenas!"));
            return;
        }

        int voidLevel;
        try {
            voidLevel = Integer.parseInt(args[1]);
            if (voidLevel < 0 || voidLevel > 256) {
                player.sendMessage(CC.translate("&cVoid level must be between 0 and 256!"));
                return;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid void level! Please enter a valid number."));
            return;
        }

        ((StandAloneArena) arena).setVoidLevel(voidLevel);
        arenaService.saveArena(arena);
        player.sendMessage(CC.translate("&aVoid level for arena &6" + arena.getName() + "&a has been set to &6" + voidLevel + "&a!"));
    }
}