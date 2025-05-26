package dev.revere.alley.feature.level.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.Level;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminViewCommand extends BaseCommand {
    @CommandData(name = "leveladmin.view", isAdminOnly = true, description = "View level information", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/leveladmin view &b<levelName>"));
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getLevelService();
        Level level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(CC.translate("&cA level with that name does not exist!"));
            return;
        }

        Arrays.asList(
                "",
                "&b&lLevel Information:",
                " &f● &bName: &e" + level.getName(),
                " &f● &bDisplay Name: &e" + level.getDisplayName(),
                " &f● &bMinimum Elo: &e" + level.getMinElo(),
                " &f● &bMaximum Elo: &e" + level.getMaxElo(),
                " &f● &bMaterial: &e" + level.getMaterial().name(),
                " &f● &bDurability: &e" + level.getDurability(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));
    }
}