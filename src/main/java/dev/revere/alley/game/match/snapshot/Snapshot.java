package dev.revere.alley.game.match.snapshot;

import dev.revere.alley.util.InventoryUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private UUID opponent;

    private double health;
    private int foodLevel;

    private final ItemStack[] armor;
    private final ItemStack[] inventory;

    private final List<String> potionEffects;

    private int potionsThrown;
    private int potionsMissed;
    private int longestCombo;
    private int totalHits;

    private long createdAt;

    /**
     * Constructor for the Snapshot class.
     *
     * @param player the player to create the snapshot for
     * @param alive  whether the player is alive or not
     */
    public Snapshot(Player player, boolean alive) {
        this.uuid = player.getUniqueId();
        this.username = player.getName();
        this.health = alive ? Math.round(player.getHealth() / 2) : 0;
        this.foodLevel = player.getFoodLevel();
        this.armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());
        this.inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        this.potionEffects = player.getActivePotionEffects().stream()
                .map(effect -> effect.getType().getName() + " " + effect.getAmplifier())
                .collect(Collectors.toList());
        this.createdAt = System.currentTimeMillis();
    }

    /**
     * Get the accuracy of potion throws as a percentage.
     *
     * @return the potion accuracy percentage
     */
    public double getPotionAccuracy() {
        if (this.potionsMissed == 0) {
            return 100.0;
        } else if (this.potionsThrown == this.potionsMissed) {
            return 50.0;
        }

        return Math.round(100.0D - (((double) this.potionsMissed / (double) this.potionsThrown) * 100.0D));
    }
}