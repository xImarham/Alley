package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.feature.arena.selection.ArenaSelection;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaToolCommand extends BaseCommand {

    @CommandData(name = "arena.tool", aliases = "arena.wand", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().first(ArenaSelection.SELECTION_TOOL) != -1) {
            player.getInventory().remove(ArenaSelection.SELECTION_TOOL);
            player.sendMessage(CC.translate("&cSelection tool has been removed."));
            player.updateInventory();
            return;
        }

        player.getInventory().addItem(ArenaSelection.SELECTION_TOOL);
        player.sendMessage(CC.translate("&aSelection tool has been added to your inventory."));
        player.updateInventory();
    }
}
