package me.emmy.alley.arena.selection;

import lombok.Data;
import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.menu.pagination.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
@Data
@Getter
public class Selection {

    public static final ItemStack SELECTION_TOOL = new ItemBuilder(Material.FEATHER)
            .name("&bSelection Tool")
            .lore("&7Use this tool to select the minimum and maximum locations.")
            .build();

    private static final String SELECTION_METADATA = "alley.selection";

    private Location minimum;
    private Location maximum;

    public Selection() {

    }

    /**
     * Creates a selection based on the player.
     *
     * @param player The player to create the selection for.
     * @return The selection created.
     */
    public static Selection createSelection(Player player) {
        if (player.hasMetadata(SELECTION_METADATA)) {
            return (Selection) player.getMetadata(SELECTION_METADATA).get(0).value();
        }

        Selection selection = new Selection();

        player.setMetadata(SELECTION_METADATA, new FixedMetadataValue(Alley.getInstance(), selection));
        return selection;
    }

    /**
     * Gets the selection of the player.
     *
     * @param player The player to get the selection of.
     */
    public static void removeSelection(Player player) {
        if (player.hasMetadata(SELECTION_METADATA)) {
            player.removeMetadata(SELECTION_METADATA, Alley.getInstance());
        }
    }

    /**
     * Checks if the player has a selection.
     *
     * @return True if the player has a selection, false otherwise.
     */
    public boolean hasSelection() {
        return minimum != null && maximum != null;
    }
}
