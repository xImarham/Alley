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
public class ArenaKitListCommand extends BaseCommand {
    @Command(name = "arena.kitlist", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena kitlist <arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("     &d&l" + arenaName + " Kit List &f(" + Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().size() + "&f)"));
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Arena Kits available."));
        }
        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().forEach(kit -> player.sendMessage(CC.translate("      &f● &d" + kit)));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");
    }
}
