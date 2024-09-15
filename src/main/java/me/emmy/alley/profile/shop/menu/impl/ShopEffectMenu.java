package me.emmy.alley.profile.shop.menu.impl;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.cosmetic.impl.killeffects.AbstractKillEffect;
import me.emmy.alley.profile.cosmetic.impl.killeffects.KillEffectRepository;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.SoundEffectRepository;
import me.emmy.alley.profile.shop.menu.ShopButton;
import me.emmy.alley.profile.shop.menu.ShopMenu;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.button.BackButton;
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

        addBorder(buttons, (byte) 15, 5);
        return buttons;
    }
}
