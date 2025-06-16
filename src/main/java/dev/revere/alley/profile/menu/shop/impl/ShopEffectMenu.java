package dev.revere.alley.profile.menu.shop.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.profile.menu.shop.ShopButton;
import dev.revere.alley.profile.menu.shop.ShopMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@AllArgsConstructor
public class ShopEffectMenu extends Menu {

    private final String cosmeticType;

    @Override
    public String getTitle(Player player) {
        return "&8Shop Menu - " + cosmeticType + "s";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        SoundEffectRepository soundEffectRepository = new SoundEffectRepository();
        List<AbstractSoundEffect> soundEffects = soundEffectRepository.getCosmetics();
        int ownedSoundEffects = (int) soundEffects.stream()
                .map(AbstractSoundEffect::getPermission)
                .filter(player::hasPermission)
                .count();

        KillEffectRepository killEffectRepository = new KillEffectRepository();
        List<AbstractKillEffect> killEffects = killEffectRepository.getCosmetics();
        int ownedKillEffects = (int) killEffects.stream()
                .map(AbstractKillEffect::getPermission)
                .filter(player::hasPermission)
                .count();

        buttons.put(0, new BackButton(new ShopMenu()));
        buttons.put(3, new ShopButton("&b&lKill Effects", new ItemStack(Material.REDSTONE), Arrays.asList(
                "",
                "&fPurchase kill effects",
                "&fto use in games.",
                "",
                "&fYou own &b" + ownedKillEffects + " &fkill effects.",
                "",
                "&aClick to view!"
        )));
        buttons.put(5, new ShopButton("&b&lSound Effects", new ItemStack(Material.FEATHER), Arrays.asList(
                "",
                "&fPurchase sound effects",
                "&fto use in games.",
                "",
                "&fYou own &b" + ownedSoundEffects + " &fsound effects.",
                "",
                "&aClick to view!"
        )));

        Alley.getInstance().getCosmeticRepository().getCosmeticRepositories().get(cosmeticType).getCosmetics().stream()
                .filter(cosmetic -> cosmetic.getIcon() != null)
                .forEach(cosmetic -> buttons.put(cosmetic.getSlot(), new ShopEffectButton(cosmetic)));

        this.addBorder(buttons, 15, 5);
        return buttons;
    }
}
