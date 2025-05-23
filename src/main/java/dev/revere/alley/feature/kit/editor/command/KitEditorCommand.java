package dev.revere.alley.feature.kit.editor.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 21:31
 */
public class KitEditorCommand extends BaseCommand {
    @CommandData(name = "kiteditor", aliases = {"kitlayout", "editkit"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&cThis feature is not yet implemented."));
    }
}
