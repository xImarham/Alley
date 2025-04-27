package dev.revere.alley.feature.cosmetic.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.feature.cosmetic.menu.button.CosmeticButton;
import dev.revere.alley.feature.cosmetic.menu.button.KillEffectButton;
import dev.revere.alley.feature.cosmetic.menu.button.SoundEffectButton;
import dev.revere.alley.profile.settings.menu.PracticeSettingsMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@AllArgsConstructor
public class CosmeticsMenu extends Menu {
    private final String cosmeticType;

    @Override
    public String getTitle(Player player) {
        return "&b&lCosmetics - " + this.cosmeticType + "s";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new PracticeSettingsMenu()));
        buttons.put(3, new KillEffectButton());
        buttons.put(5, new SoundEffectButton());

        Alley.getInstance().getCosmeticRepository().getCosmeticRepositories().get(this.cosmeticType).getCosmetics().stream()
                .filter(cosmetic -> cosmetic.getIcon() != null)
                .forEach(cosmetic -> buttons.put(cosmetic.getSlot(), new CosmeticButton(cosmetic)));

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }
}
