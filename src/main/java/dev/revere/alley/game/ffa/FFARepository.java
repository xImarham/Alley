package dev.revere.alley.game.ffa;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.game.ffa.impl.DefaultFFAMatchImpl;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.util.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class FFARepository {
    private final List<AbstractFFAMatch> matches;

    public FFARepository() {
        this.matches = new ArrayList<>();
        this.loadFFAMatches();
    }

    /**
     * Load all FFA matches
     */
    public void loadFFAMatches() {
        FileConfiguration config = Alley.getInstance().getConfigService().getFfaConfig();
        ConfigurationSection ffaConfig = config.getConfigurationSection("ffa");
        if (ffaConfig == null) {
            return;
        }

        for (String kitName : ffaConfig.getKeys(false)) {
            String name = "ffa." + kitName;

            Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
            if (kit == null) {
                Logger.logError("FFA Match (" + name + ") kit not found: " + kitName);
                continue;
            }

            String arenaName = config.getString(name + ".arena");
            Arena arena = Alley.getInstance().getArenaRepository().getArenaByName(arenaName);
            if (arena == null) {
                Logger.logError("FFA Match (" + name + ") arena not found: " + arenaName);
                continue;
            }

            int maxPlayers = config.getInt(name + ".maxPlayers");
            this.matches.add(new DefaultFFAMatchImpl(kitName, arena, kit, maxPlayers));
        }
    }

    /**
     * Save all FFA matches
     */
    public void saveFFAMatches() {
        this.matches.forEach(this::saveFFAMatch);
    }

    /**
     * Save an FFA match
     *
     * @param match The match to save
     */
    public void saveFFAMatch(AbstractFFAMatch match) {
        String name = "ffa." + match.getKit().getName();
        FileConfiguration config = Alley.getInstance().getConfigService().getFfaConfig();
        config.set(name, null);
        config.set(name + ".arena", match.getArena().getName());
        config.set(name + ".maxPlayers", match.getMaxPlayers());
        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/ffa.yml"), config);
    }

    /**
     * Create a new FFA match
     *
     * @param arena The arena the match is being played in
     * @param kit The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public void createFFAMatch(Arena arena, Kit kit, int maxPlayers) {
        DefaultFFAMatchImpl match = new DefaultFFAMatchImpl(kit.getName(), arena, kit, maxPlayers);
        matches.add(match);
        this.saveFFAMatch(match);
    }

    /**
     * Delete an FFA match
     *
     * @param match The match to delete
     */
    public void deleteFFAMatch(AbstractFFAMatch match) {
        this.matches.remove(match);
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/ffa.yml");
        config.set("ffa." + match.getKit().getName(), null);
        Alley.getInstance().getFfaRepository().saveFFAMatches();
    }

    /**
     * Get an FFA match by kit name
     *
     * @param kitName The name of the kit
     * @return The FFA match
     */
    public AbstractFFAMatch getFFAMatch(String kitName) {
        return this.matches.stream().filter(match -> match.getKit().getName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
    }

    /**
     * Get an FFA match by player
     *
     * @param player The player
     * @return The FFA match
     */
    public AbstractFFAMatch getFFAMatch(Player player) {
        return this.matches.stream().filter(match -> match.getPlayers().contains(player)).findFirst().orElse(null);
    }

    /**
     * Get an FFA match by player UUID.
     *
     * @param player The player
     * @return An Optional containing the FFA match if found, or empty if not
     */
    public Optional<AbstractFFAMatch> getMatchByPlayer(Player player) {
        return this.matches.stream()
                .filter(match -> match.getPlayers().contains(player))
                .findFirst();
    }
}