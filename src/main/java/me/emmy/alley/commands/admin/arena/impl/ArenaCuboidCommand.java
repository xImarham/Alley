package me.emmy.alley.commands.admin.arena.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.selection.Selection;
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
public class ArenaCuboidCommand extends BaseCommand {
    @Command(name = "arena.setcuboid", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena setcuboid <arenaName>"));
            return;
        }

        Selection selection = Selection.createSelection(player);
        if (!selection.hasSelection()) {
            player.sendMessage(CC.translate("&cYou must select the minimum and maximum locations for the arena."));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setMinimum(selection.getMinimum());
        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setMaximum(selection.getMaximum());
        player.sendMessage(CC.translate("&aCuboid has been set for arena &b" + arenaName + "&a!"));

        Alley.getInstance().getArenaRepository().saveArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
    }
}
