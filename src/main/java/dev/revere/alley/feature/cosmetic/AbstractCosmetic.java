package dev.revere.alley.feature.cosmetic;

import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmetic;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@Data
public abstract class AbstractCosmetic implements ICosmetic {
    private final EnumCosmeticType type;
    private final String name;
    private final String description;
    private final String permission;
    private final Material icon;
    private final int slot;
    private final int price;

    public AbstractCosmetic() {
        CosmeticData data = getClass().getAnnotation(CosmeticData.class);
        if (data != null) {
            this.type = data.type();
            this.name = data.name();
            this.description = data.description();
            this.permission = data.permission();
            this.icon = data.icon();
            this.slot = data.slot();
            this.price = data.price();
        } else {
            throw new IllegalStateException("CosmeticData annotation missing");
        }
    }


    /**
     * Gets the fully-formed permission node for this cosmetic.
     *
     * @return The full permission string.
     */
    @Override
    public String getPermission() {
        return String.format("alley.cosmetic.%s.%s", this.type.getPermissionKey(), this.permission);
    }

    /**
     * All cosmetics now need a way to be executed.
     *
     * @param player The relevant player for the cosmetic effect.
     */
    public abstract void execute(Player player);
}