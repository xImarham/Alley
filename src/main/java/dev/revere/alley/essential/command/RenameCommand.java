package dev.revere.alley.essential.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 28/05/2024 - 20:16
 * I skidded this from FlowerCore !!!!!!!!!!!!!!!!!!!!
 */
public class RenameCommand extends BaseCommand {
    @Override
    @Command(name = "rename", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(CC.translate(Alley.getInstance().getConfigService().getMessagesConfig().getString("rename-item.missing-arguments")));
            return;
        }

        String itemRename = String.join(" ", command.getArgs());

        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage(CC.translate(Alley.getInstance().getConfigService().getMessagesConfig().getString("rename-item.no-item")));
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            player.sendMessage(CC.translate("&cFailed to rename the item."));
            return;
        }

        String originalName = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : translate(itemStack.getType().name());

        itemMeta.setDisplayName(CC.translate(itemRename));
        itemStack.setItemMeta(itemMeta);

        player.updateInventory();

        String renameMessage = Alley.getInstance().getConfigService().getMessagesConfig().getString("rename-item.renamed")
                .replace("{item}", originalName)
                .replace("{renamed}", itemRename);
        player.sendMessage(CC.translate(renameMessage));
    }

    private String translate(String name) {
        return Arrays.stream(name.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
