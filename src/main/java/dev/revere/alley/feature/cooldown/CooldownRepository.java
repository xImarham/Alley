package dev.revere.alley.feature.cooldown;

import lombok.Getter;
import dev.revere.alley.feature.cooldown.enums.EnumCooldownType;
import dev.revere.alley.util.triple.impl.MutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class CooldownRepository {
    private final List<MutableTriple<UUID, EnumCooldownType, Cooldown>> cooldowns;

    public CooldownRepository() {
        this.cooldowns = new ArrayList<>();
    }

    /**
     * Add a cooldown to the repository.
     *
     * @param uuid     the uuid of the player
     * @param type     the type of cooldown
     * @param cooldown the cooldown
     */
    public void addCooldown(UUID uuid, EnumCooldownType type, Cooldown cooldown) {
        this.cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(type));
        this.cooldowns.add(new MutableTriple<>(uuid, type, cooldown));
    }

    /**
     * Get a cooldown from the repository by the player's uuid and the type of cooldown.
     *
     * @param uuid the uuid of the player
     * @param type the type of cooldown
     * @return the cooldown
     */
    public Cooldown getCooldown(UUID uuid, EnumCooldownType type) {
        return this.cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(type))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }
}