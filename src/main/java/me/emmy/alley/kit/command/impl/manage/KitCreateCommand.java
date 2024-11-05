package me.emmy.alley.kit.command.impl.manage;

import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.database.util.MongoUtility;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.KitRepository;
import me.emmy.alley.kit.settings.KitSettingRepository;
import me.emmy.alley.kit.settings.impl.*;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.util.ActionBarUtil;
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
    @Command(name = "kit.create", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit create &b<kitName>"));
            return;
        }

        String kitName = args[0];

        KitRepository kitRepository = Alley.getInstance().getKitRepository();
        if (kitRepository.getKit(kitName) != null) {
            player.sendMessage(CC.translate("&cA kit with that name already exists!"));
            return;
        }

        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        Material icon = Material.DIAMOND_SWORD;
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            icon = player.getItemInHand().getType();
        }

        kitRepository.createKit(kitName, inventory, armor, icon);
        Alley.getInstance().getProfileRepository().loadProfiles();
        player.sendMessage(CC.translate(Locale.KIT_CREATED.getMessage().replace("{kit-name}", kitName)));
        ActionBarUtil.sendMessage(player, Locale.KIT_CREATED.getMessage().replace("{kit-name}", kitName), 5);
        player.sendMessage(CC.translate("&7Additionally, all profiles have been reloaded."));
    }
}