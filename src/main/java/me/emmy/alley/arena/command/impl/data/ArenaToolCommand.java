package me.emmy.alley.arena.command.impl.data;

import me.emmy.alley.arena.selection.Selection;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaToolCommand extends BaseCommand {

    @Command(name = "arena.tool", aliases = "arena.wand", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().first(Selection.SELECTION_TOOL) != -1) {
            player.getInventory().remove(Selection.SELECTION_TOOL);
            player.sendMessage(CC.translate("&cSelection tool has been removed."));
            player.updateInventory();
            return;
        }

        player.getInventory().addItem(Selection.SELECTION_TOOL);
        player.sendMessage(CC.translate("&aSelection tool has been added to your inventory."));
        player.updateInventory();
    }
}
