package dev.revere.alley.base.arena.schematic;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.core.lifecycle.IService;
import org.bukkit.Location;

import java.io.File;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IArenaSchematicService extends IService {
    /**
     * Saves the schematic of a given arena to a file.
     *
     * @param arena         The arena to save.
     * @param schematicFile The file to save the schematic to.
     */
    void save(AbstractArena arena, File schematicFile);

    /**
     * Updates the schematic file for a given arena by re-saving it.
     *
     * @param arena The arena whose schematic needs updating.
     */
    void updateSchematic(AbstractArena arena);

    /**
     * Pastes a schematic into the world at a specific location.
     *
     * @param location      The location to paste the schematic.
     * @param schematicFile The file containing the schematic data.
     */
    void paste(Location location, File schematicFile);

    /**
     * Deletes the physical blocks of a temporary arena from the world.
     *
     * @param arena The temporary StandAloneArena to delete.
     */
    void delete(StandAloneArena arena);

    /**
     * Gets the schematic file for an arena by its name.
     *
     * @param name The name of the schematic (typically the arena name).
     * @return The File object pointing to the schematic.
     */
    File getSchematicFile(String name);

    /**
     * Gets the schematic file for a given arena instance.
     *
     * @param arena The arena.
     * @return The File object pointing to the schematic.
     */
    File getSchematicFile(AbstractArena arena);
}