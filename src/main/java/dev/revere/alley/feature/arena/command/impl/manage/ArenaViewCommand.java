package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaRepository;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 24/09/2024 - 18:29
 */
public class ArenaViewCommand extends BaseCommand {
    @CommandData(name = "arena.view", permission = "alley.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/arena view &b<arenaName>"));
            return;
        }

        ArenaRepository arenaRepository = Alley.getInstance().getArenaRepository();

        Arena arena = arenaRepository.getArenaByName(args[0]);
        if (arena == null) {
            sender.sendMessage(CC.translate("&6Usage: &e/arena view &b<arenaName>"));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&b&lArena " + arena.getName() +  " &f(" + (arena.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        sender.sendMessage(CC.translate(" &f● &bDisplay Name: &f" + arena.getDisplayName()));
        sender.sendMessage(CC.translate(" &f● &bName: &f" + arena.getName()));
        sender.sendMessage(CC.translate(" &f● &bType: &f" + arena.getType()));
        sender.sendMessage(CC.translate("   &f● &bData: &f"));
        sender.sendMessage(CC.translate("    &f● &bCenter: &f" + (arena.getCenter() != null ? arena.getCenter().getX() + ", " + arena.getCenter().getY() + ", " + arena.getCenter().getZ() + ", &7" + arena.getCenter().getPitch() + ", " + arena.getCenter().getYaw() + " &7[" + arena.getCenter().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("    &f● &bPos1: &f" + (arena.getPos1() != null ? arena.getPos1().getX() + ", " + arena.getPos1().getY() + ", " + arena.getPos1().getZ() + ", &7" + arena.getPos1().getPitch() + ", " + arena.getPos1().getYaw() + " &7[" + arena.getPos1().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("    &f● &bPos2: &f" + (arena.getPos2() != null ? arena.getPos2().getX() + ", " + arena.getPos2().getY() + ", " + arena.getPos2().getZ() + ", &7" + arena.getPos2().getPitch() + ", " + arena.getPos2().getYaw() + " &7[" + arena.getPos2().getWorld().getName() + "]" : "&cNull")));

        if (arena instanceof StandAloneArena) {
            StandAloneArena standAloneArena = (StandAloneArena) arena;
            sender.sendMessage(CC.translate("    &f● &bTeam 1 Portal: &f" + (standAloneArena.getTeam1Portal() != null ? standAloneArena.getTeam1Portal().getX() + ", " + standAloneArena.getTeam1Portal().getY() + ", " + standAloneArena.getTeam1Portal().getZ() + " &7[" + standAloneArena.getTeam1Portal().getWorld().getName() + "]" : "&cNull")));
            sender.sendMessage(CC.translate("    &f● &bTeam 2 Portal: &f" + (standAloneArena.getTeam2Portal() != null ? standAloneArena.getTeam2Portal().getX() + ", " + standAloneArena.getTeam2Portal().getY() + ", " + standAloneArena.getTeam2Portal().getZ() + " &7[" + standAloneArena.getTeam2Portal().getWorld().getName() + "]" : "&cNull")));
            sender.sendMessage(CC.translate("    &f● &bHeight Limit: &f" + standAloneArena.getHeightLimit()));
        }

        sender.sendMessage(CC.translate("    &f● &bCuboid:"));
        sender.sendMessage(CC.translate("     &f● &bMinimum: &f" + (arena.getMinimum() != null ? arena.getMinimum().getX() + ", " + arena.getMinimum().getY() + ", " + arena.getMinimum().getZ() + " &7[" + arena.getMinimum().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("     &f● &bMaximum: &f" + (arena.getMaximum() != null ? arena.getMaximum().getX() + ", " + arena.getMaximum().getY() + ", " + arena.getMaximum().getZ() + " &7[" + arena.getMaximum().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("   &f● &bKits: &f(" + arena.getKits().size() + ")"));
        if (arena.getKits().isEmpty()) {
            sender.sendMessage(CC.translate("    &f● &cNo Kits added yet."));
        } else {
            arena.getKits().forEach(kit -> sender.sendMessage(CC.translate("    &f● &b" + kit)));
        }
    }
}
