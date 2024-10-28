package me.emmy.alley.essential.command;

import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 09:15
 */
public class RemoveEnchantsCommand extends BaseCommand {
    @Command(name = "removeenchants", aliases = "enchantsremovement", permission = "alley.command.removeenchants")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().getItemInHand() == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to remove its enchantments."));
            return;
        }

        if (player.getInventory().getItemInHand().getEnchantments().isEmpty()) {
            player.sendMessage(CC.translate("&cThe item you're holding doesn't have any enchantments."));
            return;
        }

        player.sendMessage(CC.translate("&cEnchantsments: &f" + player.getInventory().getItemInHand().getEnchantments().keySet()));

        player.getInventory().getItemInHand().getEnchantments().keySet().forEach(enchant -> {
            player.getInventory().getItemInHand().removeEnchantment(enchant);
        });

        player.sendMessage(CC.translate("&aSuccessfully removed all enchantments from the &b" + player.getInventory().getItemInHand().getType().name() + " &aitem."));
    }
}