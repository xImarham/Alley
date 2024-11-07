package dev.revere.alley.profile.cosmetic.interfaces;

import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public interface ICosmeticRepository<T extends ICosmetic> {
    List<T> getCosmetics();
}