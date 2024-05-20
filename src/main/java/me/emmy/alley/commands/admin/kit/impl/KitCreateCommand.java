package me.emmy.alley.commands.admin.kit.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 20/05/2024 - 13:06
 */

public class KitCreateCommand extends BaseCommand {
    @Override
    @Command(name = "kit.create", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit create (kit-name)"));
            return;
        }

        String kitName = args[0];

        if (Alley.getInstance().getKitManager().getKitByName(kitName) != null) {
            player.sendMessage(CC.translate("&cA kit with that name already exists!"));
            return;
        }

        Kit kit = createKit(player, kitName);

        Alley.getInstance().getKitManager().getKits().add(kit);
        Alley.getInstance().getKitManager().saveConfig();

        player.sendMessage(CC.translate("&aSuccessfully created a new kit named &b" + kitName + "&a!"));
    }

    private Kit createKit(Player player, String kitName) {
        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        Material icon = Material.DIAMOND_SWORD;

        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            icon = player.getItemInHand().getType();
        }

        return new Kit(
                kitName,
                "&d" + kitName,
                "&d" + kitName + " kit description",
                true,
                0,
                0,
                0,
                inventory,
                armor,
                icon,
                (byte) 0

        );
    }
}