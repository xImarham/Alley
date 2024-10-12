package me.emmy.alley.game.event;

import me.emmy.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 12/10/2024 - 22:33
 */
public interface Event {

    String name();

    String description();

    Material icon();

    int durability();

    /**
     * Add a player to the event
     *
     * @param player the player to add
     */
    void addPlayer(Player player);

    /**
     * Remove a player from the event
     *
     * @param player the player to remove
     */
    void removePlayer(Player player);

    /**
     * Handle the death of a player
     *
     * @param player the player that died
     */
    void handleDeath(Player player);

    /**
     * Respawn a player
     *
     * @param player the player to respawn
     */
    void respawnPlayer(Player player);

    /**
     * Prepare a player for the event
     *
     * @param player the player to prepare
     */
    void preparePlayer(Player player);

    /**
     * Deny movement for a player during the event start countdown
     *
     * @param player the player to deny movement for
     */
    void denyMovement(Player player);

    void start();

    void stop();

    void end();

    /**
     * Broadcast a message to all players in the event
     *
     * @param message the message to broadcast
     */
    void broadcast(String message, List<Player> players);
}