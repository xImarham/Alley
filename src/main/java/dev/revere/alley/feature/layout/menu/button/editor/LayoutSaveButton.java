package dev.revere.alley.feature.layout.menu.button.editor;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutSaveButton extends Button {
    private final Kit kit;
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL)
                .name("&b&lSave")
                .durability(13)
                .lore(
                        "&7Save changes &",
                        "&7return to main menu.",
                        "",
                        "&aClick to save!"
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());

        LayoutData layout = profile.getProfileData().getLayoutData().getLayout(this.kit.getName(), this.layout.getName());
        layout.setDisplayName(this.layout.getDisplayName());
        layout.setItems(player.getInventory().getContents());

        Alley.getInstance().getLayoutService().getLayoutMenu().openMenu(player);
    }
}