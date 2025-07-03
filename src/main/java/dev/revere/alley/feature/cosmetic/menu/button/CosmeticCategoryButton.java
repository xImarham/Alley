package dev.revere.alley.feature.cosmetic.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.queue.IQueueService;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.menu.CosmeticTypeMenu;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.repository.ICosmeticRepository;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@RequiredArgsConstructor
public class CosmeticCategoryButton extends Button {

    private final EnumCosmeticType cosmeticType;
    private final Material icon;

    @Override
    public ItemStack getButtonItem(Player player) {
        String friendlyName = StringUtil.formatEnumName(cosmeticType);

        int totalCount = 0;
        int ownedCount = 0;

        BaseCosmeticRepository<?> repository = Alley.getInstance().getService(ICosmeticRepository.class).getRepository(cosmeticType);
        if (repository != null) {
            totalCount = repository.getCosmetics().size();
            ownedCount = (int) repository.getCosmetics().stream()
                    .filter(cosmetic -> player.hasPermission(cosmetic.getPermission()))
                    .count();
        }

        int percentage = (totalCount == 0) ? 0 : (int) (((double) ownedCount / totalCount) * 100);

        List<String> lore = new ArrayList<>();
        lore.add("&7" + cosmeticType.getDescription());
        lore.add("");
        lore.add(String.format("&fUnlocked: &6%d/%d &7(%d%%)", ownedCount, totalCount, percentage));
        lore.add("");
        lore.add("&aClick to view!");

        return new ItemBuilder(this.icon)
                .name("&6&l" + friendlyName + "s")
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.playNeutral(player);
        new CosmeticTypeMenu(cosmeticType).openMenu(player);
    }
}