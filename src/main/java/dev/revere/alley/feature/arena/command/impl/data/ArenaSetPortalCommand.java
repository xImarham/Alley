package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/03/2025
 */
public class ArenaSetPortalCommand extends BaseCommand {
    @CommandData(name = "arena.setportal", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setportal &b<name> <1/2>"));
            return;
        }

        ArenaService arenaService = this.plugin.getArenaService();
        String name = args[0];

        AbstractArena arena = arenaService.getArenaByName(name);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
            return;
        }

        if (arena.getType() != EnumArenaType.STANDALONE) {
            player.sendMessage(CC.translate("&cYou can only set portals for standalone arenas."));
            return;
        }

        String portal = args[1];
        if (!portal.equalsIgnoreCase("1") && !portal.equalsIgnoreCase("2")) {
            player.sendMessage(CC.translate("&cInvalid portal. Please use '1' or '2'."));
            return;
        }

        StandAloneArena standAloneArena = (StandAloneArena) arena;
        if (portal.equalsIgnoreCase("1")) {
            standAloneArena.setTeam1Portal(player.getLocation());
        } else if (portal.equalsIgnoreCase("2")) {
            standAloneArena.setTeam2Portal(player.getLocation());
        }

        player.sendMessage(CC.translate("&aSuccessfully set the " + portal + " portal for the arena " + name + "."));
        arena.saveArena();
    }
}