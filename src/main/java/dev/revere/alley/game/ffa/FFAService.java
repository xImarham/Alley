package dev.revere.alley.game.ffa;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.ffa.impl.DefaultFFAMatchImpl;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
@Getter
public class FFAService {
    protected final Alley plugin;
    private final List<AbstractFFAMatch> matches;

    private final List<Kit> ffaKits;
    private final int defaultPlayerSize;

    /**
     * Constructor for the FFAService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public FFAService(Alley plugin) {
        this.plugin = plugin;
        this.matches = new ArrayList<>();
        this.ffaKits = plugin.getKitService().getKits().stream().filter(Kit::isFfaEnabled).collect(Collectors.toList());
        this.defaultPlayerSize = 20;
        this.initializeMatches();
    }

    /**
     * Load all FFA matches
     */
    public void initializeMatches() {
        for (Kit kit : this.ffaKits) {
            AbstractArena arena = this.plugin.getArenaService().getArenaByName(kit.getFfaArenaName());
            if (arena == null) {
                Logger.logError("Kit " + kit.getName() + " has no FFA arena set. Please set the FFA arena in the kit settings.");
                continue;
            }

            if (kit.getMaxFfaPlayers() <= 0) {
                kit.setMaxFfaPlayers(this.defaultPlayerSize);
                Logger.logError("FFA match for kit " + kit.getName() + " has a max player size of 0. Setting to default of " + this.defaultPlayerSize + " players.");
            }

            this.createFFAMatch(arena, kit, kit.getMaxFfaPlayers());
        }
    }

    /**
     * Creates a new FFA match with the given parameters.
     *
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public void createFFAMatch(AbstractArena arena, Kit kit, int maxPlayers) {
        DefaultFFAMatchImpl match = new DefaultFFAMatchImpl(kit.getName(), arena, kit, maxPlayers);
        this.matches.add(match);
    }

    /**
     * Get an FFA match instance by the player.
     *
     * @param player The player
     * @return An Optional containing the FFA match if found, or empty if not
     */
    public Optional<AbstractFFAMatch> getMatchByPlayer(Player player) {
        return this.matches.stream().filter(match -> match.getPlayers().contains(match.getGameFFAPlayer(player))).findFirst();
    }

    /**
     * Method to get an FFA match instance by its name.
     *
     * @param kitName The name of the kit the match is using
     * @return The FFA match instance
     */
    public AbstractFFAMatch getFFAMatch(String kitName) {
        return this.matches.stream().filter(match -> match.getKit().getName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
    }

    /**
     * Get an FFA match instance by the player.
     *
     * @param player The player whose in the match.
     * @return The FFA match instance
     */
    public AbstractFFAMatch getFFAMatch(Player player) {
        return this.matches.stream().filter(match -> match.getPlayers().contains(match.getGameFFAPlayer(player))).findFirst().orElse(null);
    }

    public void reloadFFAKits() {
        this.matches.forEach(match -> match.getPlayers().forEach(ffaPlayer -> {
            ffaPlayer.getPlayer().sendMessage(CC.translate("&cThe FFA match has been reloaded. Please rejoin."));
            match.leave(ffaPlayer.getPlayer());
        }));

        this.matches.clear();
        this.ffaKits.clear();

        this.ffaKits.addAll(this.plugin.getKitService().getKits().stream().filter(Kit::isFfaEnabled).collect(Collectors.toList()));
        this.ffaKits.forEach(kit -> {
            AbstractArena arena = this.plugin.getArenaService().getArenaByName(kit.getFfaArenaName());
            if (arena == null) {
                Logger.logError("Kit " + kit.getName() + " has no FFA arena set. Please set the FFA arena in the kit settings.");
                return;
            }

            if (kit.getMaxFfaPlayers() <= 0) {
                kit.setMaxFfaPlayers(this.defaultPlayerSize);
                Logger.logError("FFA match for kit " + kit.getName() + " has a max player size of 0. Setting to default of " + this.defaultPlayerSize + " players.");
            }

            this.createFFAMatch(arena, kit, kit.getMaxFfaPlayers());
        });
    }
}