package me.emmy.alley.kiteditor.command;

import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 21:31
 */
public class KitEditorCommand extends BaseCommand {
    @Command(name = "kiteditor", aliases = {"kitlayout", "editkit"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&cWelcome to the place of where Emmy spends most of her time!"));
    }
}
