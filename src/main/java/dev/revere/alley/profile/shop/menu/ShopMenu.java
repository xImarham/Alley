package dev.revere.alley.profile.shop.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.profile.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.profile.cosmetic.impl.killeffects.KillEffectRepository;
import dev.revere.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.profile.cosmetic.impl.soundeffect.SoundEffectRepository;
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
public class ShopMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&8Shop Menu";
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

        buttons.put(21, new ShopButton("&b&lKill Effects", new ItemStack(Material.REDSTONE), Arrays.asList(
                "",
                "&fPurchase kill effects",
                "&fto use in games.",
                "",
                "&fYou own &b" + ownedKillEffects + " &fkill effects.",
                "",
                "&aClick to view!"
        )));

        buttons.put(23, new ShopButton("&b&lSound Effects", new ItemStack(Material.FEATHER), Arrays.asList(
                "",
                "&fPurchase sound effects",
                "&fto use in games.",
                "",
                "&fYou own &b" + ownedSoundEffects + " &fsound effects.",
                "",
                "&aClick to view!"
        )));

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
