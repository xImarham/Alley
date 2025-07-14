package dev.revere.alley.base.kit.command.helper.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.tool.reflection.utility.ReflectionUtility;
import dev.revere.alley.util.chat.CC;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * @author Emmy
 * @project Alley
 * @date 14/07/2025
 */
public class UnbreakableCommand extends BaseCommand {
    @CommandData(
            name = "unbreakable",
            description = "Set the unbreakable state of the item in your hand",
            usage = "unbreakable <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/unbreakable &6<true|false>"));
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to modify unbreakable state."));
            return;
        }

        boolean unbreakable = Boolean.parseBoolean(args[0]);

        ItemStack unbreakAbleItem = ReflectionUtility.setUnbreakable(item, unbreakable);
        player.setItemInHand(unbreakAbleItem);

        player.sendMessage(CC.translate("&aSuccessfully set the unbreakable state of the item to &6" + unbreakable + "&a."));
    }
}