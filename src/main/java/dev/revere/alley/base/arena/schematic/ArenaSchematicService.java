package dev.revere.alley.base.arena.schematic;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class ArenaSchematicService {
    protected final Alley plugin;

    //TODO: The arenas currently have max and min locations,
    // but they are representing the game field, rather than the actual arena boundaries,
    // which is why the schematics are not covering the entire arena.

    /**
     * Constructor for the ArenaSchematicService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ArenaSchematicService(Alley plugin) {
        this.plugin = plugin;
        this.convertArenasToSchematic();
    }

    public void convertArenasToSchematic() {
        List<AbstractArena> arenas = this.plugin.getArenaService().getArenas();

        arenas.forEach(arena -> {
                    File schematicFile = new File(this.plugin.getDataFolder(), "schematics/" + arena.getName() + ".schematic");
                    if (!schematicFile.exists()) {
                        schematicFile.getParentFile().mkdirs();
                        this.save(arena, schematicFile);
                    }
                }
        );
    }

    /**
     * Saves the schematic of the given arena to the specified file.
     *
     * @param arena         The arena to save.
     * @param schematicFile The file to save the schematic to.
     */
    public void save(AbstractArena arena, File schematicFile) {
        try {
            Location min = arena.getMinimum();
            Location max = arena.getMaximum();

            World bukkitWorld = min.getWorld();
            CuboidSelection selection = new CuboidSelection(bukkitWorld, min, max);

            Vector minVector = BukkitUtil.toVector(selection.getMinimumPoint());
            Vector maxVector = BukkitUtil.toVector(selection.getMaximumPoint());

            CuboidClipboard clipboard = new CuboidClipboard(maxVector.subtract(minVector).add(Vector.ONE), minVector);
            EditSession session = new EditSession(new BukkitWorld(bukkitWorld), -1);
            clipboard.copy(session);

            SchematicFormat.MCEDIT.save(clipboard, schematicFile);

            Logger.log("Saved schematic for arena: " + arena.getName());
        } catch (Exception exception) {
            Logger.logException("Failed to save schematic for arena " + arena.getName(), exception);
        }
    }

    /**
     * Pastes the schematic at the specified location.
     *
     * @param location      The location to paste the schematic.
     * @param schematicFile The file containing the schematic to paste.
     */
    public void paste(Location location, File schematicFile) {
        try {
            org.bukkit.World bukkitWorld = location.getWorld();
            Vector toVector = BukkitUtil.toVector(location);
            EditSession session = new EditSession(BukkitUtil.getLocalWorld(bukkitWorld), -1);

            SchematicFormat format = MCEditSchematicFormat.getFormat(schematicFile);
            CuboidClipboard clipboard = format.load(schematicFile);

            clipboard.paste(session, toVector, false);

            Logger.log("Pasted schematic at location: " + location);
        } catch (Exception exception) {
            Logger.logException("Failed to paste schematic at " + location.toString(), exception);
        }
    }

    /**
     * Returns the schematic file by name from the schematics directory.
     *
     * @param name The name of the schematic (without extension).
     * @return The File representing the schematic.
     */
    public File getSchematicFile(String name) {
        return new File(this.plugin.getDataFolder(), "schematics/" + name.toLowerCase().replace(" ", "_") + ".schematic");
    }

    /**
     * Returns the schematic file associated with the given arena.
     *
     * @param arena The arena to get the schematic for.
     * @return The File representing the schematic associated with the arena.
     */
    public File getSchematicFile(AbstractArena arena) {
        return this.getSchematicFile(arena.getName());
    }
}