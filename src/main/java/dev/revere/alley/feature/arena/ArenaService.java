package dev.revere.alley.feature.arena;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.feature.arena.impl.FreeForAllArena;
import dev.revere.alley.feature.arena.impl.SharedArena;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.tool.serializer.Serializer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

    /**
     * Constructor for the ArenaService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ArenaService(Alley plugin) {
        this.plugin = plugin;
        this.arenas = new ArrayList<>();
        this.loadArenas();
    }

    /**
     * Method to load all arenas from the arenas.yml file.
     */
    public void loadArenas() {
        FileConfiguration config = this.plugin.getConfigService().getConfig("storage/arenas.yml");

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
                .filter(arena -> !(arena instanceof StandAloneArena) || !((StandAloneArena) arena).isActive())
                .collect(Collectors.toList());

        if (availableArenas.isEmpty()) {
            return null;
        }

        AbstractArena selectedArena = availableArenas.get(ThreadLocalRandom.current().nextInt(availableArenas.size()));
        if (selectedArena instanceof StandAloneArena) {
            ((StandAloneArena) selectedArena).setActive(true);
        }
        return selectedArena;
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