package dev.revere.alley.feature.level.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.Level;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminDeleteCommand extends BaseCommand {
    @CommandData(name = "leveladmin.delete", isAdminOnly = true, description = "Delete a level", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/leveladmin delete &b<levelName>"));
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getLevelService();
        Level level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(CC.translate("&cA level with that name does not exist!"));
            return;
        }

        levelService.deleteLevel(level);
        sender.sendMessage(CC.translate("&cSuccessfully deleted level &b" + levelName + "&c!"));
    }
}