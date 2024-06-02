package me.emmy.alley.profile.cosmetic.interfaces;

import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public interface ICosmetic {
    String getName();
    String getDescription();
    String getPermission();
    Material getIcon();
    int getPrice();
    int getSlot();
}
