package dev.revere.alley.essential.command;

import dev.revere.alley.util.EnchantUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 28/05/2024 - 20:28
 */
public class EnchantCommand extends BaseCommand {
    @CommandData(name = "enchant", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /enchant (enchantment) (level)"));
            return;
        }

        Enchantment enchantment = EnchantUtil.getEnchantment(args[0]);
        if (enchantment == null) {
            player.sendMessage(CC.translate("&cInvalid enchantment name!"));
            player.sendMessage(CC.translate("&cValid enchantments: &7" + EnchantUtil.getSortedEnchantments().toUpperCase()));
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cEnchantment level must be a number!"));
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to enchant!"));
            return;
        }

        String displayName = itemInHand.getItemMeta().getDisplayName() == null ? itemInHand.getType().name() : itemInHand.getItemMeta().getDisplayName();

        itemInHand.addUnsafeEnchantment(enchantment, level);
        player.sendMessage(CC.translate("&aSuccessfully enchanted the &b" + displayName + " &aitem with &b" + enchantment.getName() + " &alevel &b" + level + "&a!"));
    }
}