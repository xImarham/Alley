package dev.revere.alley.feature.layout;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.kit.enums.EnumKitCategory;
import dev.revere.alley.feature.layout.menu.LayoutMenu;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

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
}