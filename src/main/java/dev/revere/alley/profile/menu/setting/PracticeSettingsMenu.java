package dev.revere.alley.profile.menu.setting;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileSettingData;
import dev.revere.alley.profile.menu.setting.button.PracticeSettingsButton;
import dev.revere.alley.profile.menu.setting.enums.EnumPracticeSettingType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
public class PracticeSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lPractice Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        ProfileSettingData settings = profile.getProfileData().getSettingData();

        for (EnumPracticeSettingType type : EnumPracticeSettingType.values()) {
            buttons.put(type.slot, new PracticeSettingsButton(
                    type.displayName,
                    type.material,
                    type.durability,
                    type.loreProvider.apply(settings)
            ));
        }

        this.addBorder(buttons, (byte) 15, 4);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }
}