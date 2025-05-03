package dev.revere.alley.profile.data.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.layout.record.LayoutRecord;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 02/05/2025
 */
@Getter
@Setter
public class ProfileLayoutData {
    private List<LayoutRecord> layouts;

    public ProfileLayoutData() {
        this.layouts = new ArrayList<>();
        this.addKitLayouts();
    }

    /**
     * Adds default kit layouts to the list.
     */
    private void addKitLayouts() {
        List<Kit> kits = Alley.getInstance().getKitService().getKits();
        for (Kit kit : kits) {
            this.layouts.add(new LayoutRecord("Layout1", "Layout 1", kit.getInventory()));
        }
    }

    /**
     * Adds a new layout to the list.
     *
     * @param name        the name of the layout.
     * @param displayName the display name of the layout.
     * @param items       the items in the layout.
     */
    public void addLayout(String name, String displayName, ItemStack[] items) {
        this.layouts.add(new LayoutRecord(name, displayName, items));
    }

    /**
     * Retrieves the layout by its name.
     *
     * @param layoutName the name of the layout.
     * @return the LayoutRecord object if found, null otherwise.
     */
    public LayoutRecord getLayout(String layoutName) {
        return this.layouts.stream()
                .filter(layout -> layout.getName().equalsIgnoreCase(layoutName))
                .findFirst()
                .orElse(null);
    }
}