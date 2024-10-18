package me.emmy.alley.arena;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.impl.FreeForAllArena;
import me.emmy.alley.arena.impl.SharedArena;
import me.emmy.alley.arena.impl.StandAloneArena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.location.LocationUtil;
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
public class ArenaRepository {
    private final List<Arena> arenas = new ArrayList<>();

    public ArenaRepository() {
        loadArenas();
    }
    /**
     * Load all arenas from the arenas.yml file
     */
    public void loadArenas() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/arenas.yml");

        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");
        if (arenasConfig == null) {
            return;
        }

        for (String arenaName : arenasConfig.getKeys(false)) {
            String name = "arenas." + arenaName;

            ArenaType arenaType = ArenaType.valueOf(config.getString(name + ".type"));
            Location minimum = LocationUtil.deserialize(config.getString(name + ".minimum"));
            Location maximum = LocationUtil.deserialize(config.getString(name + ".maximum"));

            Arena arena;
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
                            maximum
                    );
                    break;
                case FFA:
                    Location safeZonePos1 = LocationUtil.deserialize(config.getString(name + ".safezone.pos1"));
                    Location safeZonePos2 = LocationUtil.deserialize(config.getString(name + ".safezone.pos2"));

                    arena = new FreeForAllArena(
                            arenaName,
                            safeZonePos1,
                            safeZonePos2
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + arenaType);
            }

            if (config.contains(name + ".kits")) {
                for (String kitName : config.getStringList(name + ".kits")) {
                    if (Alley.getInstance().getKitRepository().getKit(kitName) != null) {
                        arena.getKits().add(kitName);
                    }
                }
            }

            if (config.contains(name + ".pos1")) {
                arena.setPos1(LocationUtil.deserialize(config.getString(name + ".pos1")));
            }

            if (config.contains(name + ".pos2")) {
                arena.setPos2(LocationUtil.deserialize(config.getString(name + ".pos2")));
            }

            if (config.contains(name + ".center")) {
                arena.setCenter(LocationUtil.deserialize(config.getString(name + ".center")));
            }

            if (config.contains(name + ".displayName")) {
                arena.setDisplayName(config.getString(name + ".displayName"));
            }

            if (config.contains(name + ".enabled")) {
                arena.setEnabled(config.getBoolean(name + ".enabled"));
            }

            arenas.add(arena);
        }
    }

    /**
     * Save all arenas to the arenas.yml file
     */
    public void saveArenas() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/arenas.yml");

        for (Arena arena : arenas) {
            String name = "arenas." + arena.getName();

            config.set(name + ".type", arena.getType().name());
            config.set(name + ".minimum", LocationUtil.serialize(arena.getMinimum()));
            config.set(name + ".maximum", LocationUtil.serialize(arena.getMaximum()));
            config.set(name + ".kits", arena.getKits());
            config.set(name + ".pos1", LocationUtil.serialize(arena.getPos1()));
            config.set(name + ".pos2", LocationUtil.serialize(arena.getPos2()));
            config.set(name + ".center", LocationUtil.serialize(arena.getCenter()));
            config.set(name + ".displayName", arena.getDisplayName());
            config.set(name + ".enabled", arena.isEnabled());
        }

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFile("storage/arenas.yml"), config);
    }

    /**
     * Save an arena
     *
     * @param arena the arena to save
     */
    public void saveArena(Arena arena) {
        arena.saveArena();
    }

    /**
     * Delete an arena
     *
     * @param arena the arena to delete
     */
    public void deleteArena(Arena arena) {
        arena.deleteArena();
    }

    /**
     * Get an arena by its name
     *
     * @param name the name of the arena
     * @return the arena
     */
    public Arena getArenaByName(String name) {
        return arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Get an arena by its class
     *
     * @param clazz the class of the arena
     * @return the arena
     */
    public Arena getArenaByClass(Class<? extends Arena> clazz) {
        return arenas.stream().filter(arena -> arena.getClass().equals(clazz)).findFirst().orElse(null);
    }

    /**
     * Get a random arena by its kit
     *
     * @param kit the kit
     * @return the arena
     */
    public Arena getRandomArena(Kit kit) {
        List<Arena> availableArenas = arenas.stream()
                .filter(arena -> arena.getKits().contains(kit.getName()))
                .filter(Arena::isEnabled)
                .filter(arena -> !(arena instanceof StandAloneArena) || !((StandAloneArena) arena).isActive())
                .collect(Collectors.toList());

        if (availableArenas.isEmpty()) {
            return null;
        }

        Arena selectedArena = availableArenas.get(ThreadLocalRandom.current().nextInt(availableArenas.size()));
        if (selectedArena instanceof StandAloneArena) {
            ((StandAloneArena) selectedArena).setActive(true);
        }
        return selectedArena;
    }
}