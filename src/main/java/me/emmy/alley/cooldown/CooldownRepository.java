package me.emmy.alley.cooldown;

import lombok.Getter;
import me.emmy.alley.cooldown.enums.EnumCooldownType;
import me.emmy.alley.util.MutableTriple;

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

    public void addCooldown(UUID uuid, EnumCooldownType type, Cooldown cooldown) {
        cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(type));
        cooldowns.add(new MutableTriple<>(uuid, type, cooldown));
    }

    public Cooldown getCooldown(UUID uuid, EnumCooldownType type) {
        return cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(type))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }
}