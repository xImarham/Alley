package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaRepository;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/03/2025
 */
public class ArenaSetHeightLimit extends BaseCommand {
    @CommandData(name = "arena.setheightlimit", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setheightlimit &b<arenaName>"));
            return;
        }

        ArenaRepository arenaRepository = this.plugin.getArenaRepository();
        Arena arena = arenaRepository.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arena.getType() != EnumArenaType.STANDALONE) {
            player.sendMessage(CC.translate("&cYou can only set the height limit for standalone arenas!"));
            return;
        }

        int limit = player.getLocation().getBlockY();
        ((StandAloneArena) arena).setHeightLimit(limit);
        arenaRepository.saveArena(arena);
        player.sendMessage(CC.translate("&aHeight limit for arena &b" + arena.getName() + "&a has been set to &b" + limit + "&a!"));
    }
}