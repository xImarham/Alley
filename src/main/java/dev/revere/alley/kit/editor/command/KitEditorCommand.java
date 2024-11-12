package dev.revere.alley.kit.editor.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.util.chat.CC;
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

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_CMD));
    }
}
