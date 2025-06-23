package dev.revere.alley.profile.menu.shop;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import dev.revere.alley.profile.menu.shop.button.ShopItemButton;
import dev.revere.alley.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@RequiredArgsConstructor
public class ShopCategoryMenu extends Menu {

    private final EnumCosmeticType cosmeticType;

    @Override
    public String getTitle(Player player) {
        return "&8Shop - " + StringUtil.formatCosmeticTypeName(cosmeticType) + "s";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(new ShopMenu()));

        BaseCosmeticRepository<?> repository = Alley.getInstance().getCosmeticRepository().getRepository(cosmeticType);
        if (repository != null) {
            repository.getCosmetics().stream()
                    .filter(cosmetic -> cosmetic.getIcon() != null && cosmetic.getPrice() > 0)
                    .forEach(cosmetic -> buttons.put(cosmetic.getSlot(), new ShopItemButton(cosmetic)));
        }

        return buttons;
    }
}