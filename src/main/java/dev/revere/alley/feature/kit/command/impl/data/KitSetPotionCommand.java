package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 14/03/2025
 */
public class KitSetPotionCommand extends BaseCommand {

    @CommandData(name = "kit.setpotion", aliases = {"kit.setpotioneffects"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setpotion &b<kitName>"));
            return;
        }

        String kitName = args[0];
        Kit kit = this.alley.getKitRepository().getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        ItemStack itemInHand = sender.getInventory().getItemInHand();
        if (itemInHand == null || itemInHand.getType() != Material.POTION) {
            sender.sendMessage(CC.translate("&cYou must hold a potion bottle to set effects for this kit!"));
            return;
        }

        if (!(itemInHand.getItemMeta() instanceof PotionMeta)) {
            sender.sendMessage(CC.translate("&cInvalid potion!"));
            return;
        }

        PotionMeta potionMeta = (PotionMeta) itemInHand.getItemMeta();
        List<PotionEffect> effects = potionMeta.getCustomEffects();

        if (effects.isEmpty()) {
            sender.sendMessage(CC.translate("&cThe potion you are holding has no custom effects!"));
            return;
        }

        kit.setPotionEffects(effects);
        this.alley.getKitRepository().saveKit(kit);
        sender.sendMessage(CC.translate("&aSuccessfully set potion effects for kit &e" + kitName + "&a!"));
    }
}

