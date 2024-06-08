package me.emmy.alley.kit.command.impl.manage;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.KitRepository;
import me.emmy.alley.kit.settings.KitSettingRepository;
import me.emmy.alley.locale.Locale;
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

        if (Alley.getInstance().getKitRepository().getKit(kitName) != null) {
            player.sendMessage(CC.translate("&cA kit with that name already exists!"));
            return;
        }

        Kit kit = createKit(player, kitName);

        Alley.getInstance().getKitRepository().getKits().add(kit);
        Alley.getInstance().getKitRepository().saveKit(kit);

        player.sendMessage(CC.translate(Locale.KIT_CREATED.getMessage().replace("{kit-name}", kitName)));
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
                "&7" + kitName + " kit description",
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