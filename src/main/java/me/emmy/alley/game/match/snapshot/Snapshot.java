package me.emmy.alley.game.match.snapshot;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@Getter
@Setter
public class Snapshot {
    private final String username;
    private final UUID uuid;
    private double health;
    private int foodLevel;

    private UUID opponent;

    private final ItemStack[] armor;
    private final ItemStack[] inventory;

    /**
     * Constructor for the Snapshot.
     *
     * @param player the player to create the snapshot for
     * @param alive  whether the player is alive or not
     */
    public Snapshot(Player player, boolean alive) {
        this.uuid = player.getUniqueId();
        this.username = player.getName();
        this.health = alive ? Math.round(player.getHealth() / 2) : 0;
        this.foodLevel = player.getFoodLevel();
        this.armor = player.getInventory().getArmorContents();
        this.inventory = player.getInventory().getContents();
    }
}