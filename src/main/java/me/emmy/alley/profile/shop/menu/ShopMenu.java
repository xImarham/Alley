package me.emmy.alley.profile.shop.menu;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.cosmetic.impl.killeffects.AbstractKillEffect;
import me.emmy.alley.profile.cosmetic.impl.killeffects.KillEffectRepository;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.SoundEffectRepository;
import me.emmy.alley.profile.menu.ProfileMenu;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.button.BackButton;
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

        buttons.put(0, new BackButton(new ProfileMenu()));
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
