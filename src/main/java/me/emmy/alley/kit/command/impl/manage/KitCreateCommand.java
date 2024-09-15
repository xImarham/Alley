package me.emmy.alley.kit.command.impl.manage;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.*;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 13:06
 */
public class KitCreateCommand extends BaseCommand {
    @Override
    @Command(name = "kit.create", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit create (kit-name)"));
            return;
        }

        String kitName = args[0];

        if (Alley.getInstance().getKitRepository().getKit(kitName) != null) {
            player.sendMessage(CC.translate("&cA kit with that name already exists!"));
            return;
        }

        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        Material icon = Material.DIAMOND_SWORD;

        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            icon = player.getItemInHand().getType();
        }

        Kit kit = createKit(kitName, inventory, armor, icon);

        Alley.getInstance().getKitRepository().getKits().add(kit);
        Alley.getInstance().getKitRepository().saveKit(kit);

        player.sendMessage(CC.translate(Locale.KIT_CREATED.getMessage().replace("{kit-name}", kitName)));
    }

    private Kit createKit(String kitName, ItemStack[] inventory, ItemStack[] armor, Material icon) {
        Kit kit = new Kit(
                kitName,
                "&b" + kitName,
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

        kit.addKitSetting(new KitSettingBoxingImpl());
        kit.addKitSetting(new KitSettingBuildImpl());
        kit.addKitSetting(new KitSettingRankedImpl());
        kit.addKitSetting(new KitSettingSpleefImpl());
        kit.addKitSetting(new KitSettingSumoImpl());

        return kit;
    }
}
