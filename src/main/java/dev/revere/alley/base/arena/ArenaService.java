package dev.revere.alley.base.arena;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.arena.impl.FreeForAllArena;
import dev.revere.alley.base.arena.impl.SharedArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.tool.serializer.Serializer;
import dev.revere.alley.util.VoidChunkGeneratorImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 16:54
 */
@Getter
public class ArenaService {
    protected final Alley plugin;
    private final List<AbstractArena> arenas;
    private final List<StandAloneArena> temporaryArenas;
    private World temporaryWorld;
    private Location nextCopyLocation;
    private final int arenaSpacing = 500;
    private final AtomicInteger copyIdCounter;

    /**
     * Constructor for the ArenaService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ArenaService(Alley plugin) {
        this.plugin = plugin;
        this.arenas = new ArrayList<>();
        this.temporaryArenas = new ArrayList<>();
        this.copyIdCounter = new AtomicInteger(0);
        this.loadArenas();
        initializeTemporaryWorld();
    }

    private void initializeTemporaryWorld() {
        String worldName = "temporary_arena_world";

        World existingWorld = this.plugin.getServer().getWorld(worldName);
        if (existingWorld != null) {
            this.plugin.getServer().unloadWorld(existingWorld, false);
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            this.plugin.getServer().unloadWorld(worldName, false);
            this.plugin.getServer().getWorlds().removeIf(world -> world.getName().equalsIgnoreCase(worldName));
            worldFolder.delete();
        }

        WorldCreator creator = new WorldCreator(worldName);
        creator.generateStructures(false).generator(new VoidChunkGeneratorImpl());

        this.temporaryWorld = creator.createWorld();
        this.nextCopyLocation = new Location(temporaryWorld, 0, 100, 0);
    }

    /**
     * Method to load all arenas from the arenas.yml file.
     */
    public void loadArenas() {
        FileConfiguration config = this.plugin.getConfigService().getArenasConfig();

        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");
        if (arenasConfig == null) {
            return;
        }

        for (String arenaName : arenasConfig.getKeys(false)) {
            String name = "arenas." + arenaName;

            EnumArenaType arenaType = EnumArenaType.valueOf(config.getString(name + ".type"));
            Location minimum = Serializer.deserializeLocation(config.getString(name + ".minimum"));
            Location maximum = Serializer.deserializeLocation(config.getString(name + ".maximum"));

            AbstractArena arena;
            switch (arenaType) {
                case SHARED:
                    arena = new SharedArena(
                            arenaName,
                            minimum,
                            maximum
                    );
                    break;
                case STANDALONE:
                    arena = new StandAloneArena(
                            arenaName,
                            minimum,
                            maximum,
                            Serializer.deserializeLocation(config.getString(name + ".team-one-portal")),
                            Serializer.deserializeLocation(config.getString(name + ".team-two-portal")),
                            config.getInt(name + ".height-limit")
                    );
                    break;
                case FFA:
                    arena = new FreeForAllArena(
                            arenaName,
                            Serializer.deserializeLocation(config.getString(name + ".safe-zone.pos1")),
                            Serializer.deserializeLocation(config.getString(name + ".safe-zone.pos2"))
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + arenaType);
            }

            if (config.contains(name + ".kits")) {
                for (String kitName : config.getStringList(name + ".kits")) {
                    if (this.plugin.getKitService().getKit(kitName) != null) {
                        arena.getKits().add(kitName);
                    }
                }
            }

            if (config.contains(name + ".pos1")) {
                arena.setPos1(Serializer.deserializeLocation(config.getString(name + ".pos1")));
            }

            if (config.contains(name + ".pos2")) {
                arena.setPos2(Serializer.deserializeLocation(config.getString(name + ".pos2")));
            }

            if (config.contains(name + ".center")) {
                arena.setCenter(Serializer.deserializeLocation(config.getString(name + ".center")));
            }

            if (config.contains(name + ".display-name")) {
                arena.setDisplayName(config.getString(name + ".display-name"));
            }

            if (config.contains(name + ".enabled")) {
                arena.setEnabled(config.getBoolean(name + ".enabled"));
            }

            this.arenas.add(arena);
        }
    }

    public StandAloneArena createTemporaryArenaCopy(StandAloneArena originalArena) {
        if (originalArena.isTemporaryCopy()) {
            throw new IllegalArgumentException("Cannot create a temporary copy of a temporary arena.");
        }

        int copyId = copyIdCounter.incrementAndGet();
        Location copyLocation = getNextCopyLocationForArena(originalArena);

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();
        Location originalMax = originalArena.getMaximum();

        if (originalPos1 != null && originalMin != null && originalMax != null) {
            int actualMinY = Math.min(originalMin.getBlockY(), originalMax.getBlockY());

            int pos1OffsetFromActualMin = originalPos1.getBlockY() - actualMinY;

            int targetMinY = 100 - pos1OffsetFromActualMin;
            copyLocation.setY(targetMinY);
        }

        StandAloneArena copiedArena = originalArena.createCopy(temporaryWorld, copyLocation, copyId);
        copiedArena.setHeightLimit(copiedArena.getPos1().getBlockY() + copiedArena.getHeightLimit());

        this.plugin.getArenaSchematicService().paste(copyLocation, this.plugin.getArenaSchematicService().getSchematicFile(originalArena.getName()));

        temporaryArenas.add(copiedArena);

        return copiedArena;
    }

    public Location getNextCopyLocationForArena(StandAloneArena originalArena) {
        Location location = this.nextCopyLocation.clone();

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();

        if (originalPos1 != null && originalMin != null) {
            int pos1OffsetFromMin = originalPos1.getBlockY() - originalMin.getBlockY();
            location.setY(100 - pos1OffsetFromMin);
        }

        nextCopyLocation.add(arenaSpacing, 0, 0);
        if (nextCopyLocation.getX() > arenaSpacing * 10) {
            nextCopyLocation.setX(0);
            nextCopyLocation.add(0, 0, arenaSpacing);
        }

        return location;
    }

    public void removeTemporaryArena(StandAloneArena arena) {
        temporaryArenas.remove(arena);
    }

    public void cleanupTemporaryArenas() {
        for (StandAloneArena arena : new ArrayList<>(temporaryArenas)) {
            arena.deleteCopiedArena();;
        }
        temporaryArenas.clear();
    }

    public void shutdown() {
        cleanupTemporaryArenas();

        if (temporaryWorld != null) {
            Bukkit.unloadWorld(temporaryWorld, false);
            File worldFolder = temporaryWorld.getWorldFolder();
            if (worldFolder.exists()) {
                for (File file : Objects.requireNonNull(worldFolder.listFiles())) {
                    if (!file.isDirectory() || !file.getName().equals("uid.dat")) {
                        file.delete();
                    }
                }
                try {
                    worldFolder.delete();
                } catch (Exception e) {
                    this.plugin.getLogger().warning("Failed to delete temporary world folder: " + worldFolder.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Save an arena
     *
     * @param arena the arena to save
     */
    public void saveArena(AbstractArena arena) {
        arena.saveArena();
    }

    /**
     * Delete an arena
     *
     * @param arena the arena to delete
     */
    public void deleteArena(AbstractArena arena) {
        arena.deleteArena();
    }

    /**
     * Get an arena by its name
     *
     * @param name the name of the arena
     * @return the arena
     */
    public AbstractArena getArenaByName(String name) {
        return this.arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Get a random arena by its kit
     *
     * @param kit the kit
     * @return the arena
     */
    public AbstractArena getRandomArena(Kit kit) {
        List<AbstractArena> availableArenas = this.arenas.stream()
                .filter(arena -> arena.getKits().contains(kit.getName()))
                .filter(AbstractArena::isEnabled)
                .collect(Collectors.toList());

        if (availableArenas.isEmpty()) {
            return null;
        }

        AbstractArena selectedArena = availableArenas.get(ThreadLocalRandom.current().nextInt(availableArenas.size()));
        if (selectedArena instanceof StandAloneArena) {
            return createTemporaryArenaCopy((StandAloneArena) selectedArena);
        }
        return selectedArena;
    }

    public AbstractArena selectArenaWithPotentialTemporaryCopy(AbstractArena arena) {
        if (arena instanceof StandAloneArena) {
            return createTemporaryArenaCopy((StandAloneArena) arena);
        }
        return arena;
    }

    public AbstractArena getTemporaryArena(AbstractArena arena) {
        AbstractArena mutableArena = this.getArenaByName(arena.getName());

        if (!(mutableArena instanceof StandAloneArena)) {
            return null;
        }

        return createTemporaryArenaCopy((StandAloneArena) mutableArena);
    }

    /**
     * Get a random arena of type StandAlone
     *
     * @return the arena
     */
    public AbstractArena getRandomStandAloneArena() {
        List<AbstractArena> availableArenas = this.arenas.stream()
                .filter(arena -> arena.getType() == EnumArenaType.STANDALONE)
                .filter(AbstractArena::isEnabled)
                .collect(Collectors.toList());

        if (availableArenas.isEmpty()) {
            return null;
        }

        return availableArenas.get(ThreadLocalRandom.current().nextInt(availableArenas.size()));
    }
}