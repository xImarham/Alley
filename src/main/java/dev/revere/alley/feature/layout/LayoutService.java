package dev.revere.alley.feature.layout;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.kit.enums.EnumKitCategory;
import dev.revere.alley.feature.layout.menu.LayoutMenu;
import dev.revere.alley.feature.layout.record.LayoutRecord;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@Getter
@Setter
public class LayoutService {
    protected final Alley plugin;
    private final Menu layoutMenu;

    /**
     * Constructor for the LayoutService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public LayoutService(Alley plugin) {
        this.plugin = plugin;
        this.layoutMenu = this.determineMenu();
    }

    private Menu determineMenu() {
        FileConfiguration config = Alley.getInstance().getConfigService().getMenusConfig();
        String menuType = config.getString("layout-menu.type");

        switch (menuType) {
            case "MODERN":
                Logger.logError("Modern layout menu is not implemented yet. Defaulting to classic layout menu.");
                return new LayoutMenu(EnumKitCategory.NORMAL);
            case "DEFAULT":
                return new LayoutMenu(EnumKitCategory.NORMAL);
        }

        Logger.logError("Invalid layout menu type specified in config.yml. Defaulting to modern layout menu.");
        return new LayoutMenu(EnumKitCategory.NORMAL);
    }

    /**
     * Method to initiate a book item representing a layout.
     *
     * @param layout The layout record.
     * @return The ItemStack representing the layout book.
     */
    public ItemStack getLayoutBook(LayoutRecord layout) {
        return new ItemBuilder(Material.BOOK)
                .name(layout.getDisplayName())
                .lore("&7Click to select this layout.")
                .hideMeta().build();
    }

    /**
     * Method to give the player the layout books for a specific kit.
     *
     * @param player  The player to give the books to.
     * @param kitName The name of the kit.
     */
    public void giveBooks(Player player, String kitName) {
        this.plugin.getProfileService().getProfile(player.getUniqueId()).getProfileData().getLayoutData().getLayouts().get(kitName).forEach(layout -> {
            player.getInventory().addItem(this.getLayoutBook(layout));
        });
    }
}