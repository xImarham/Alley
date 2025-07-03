package dev.revere.alley.base.cooldown;

import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.tool.triple.impl.MutableTriple;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Service(provides = ICooldownRepository.class, priority = 200)
public class CooldownRepository implements ICooldownRepository {
    private final List<MutableTriple<UUID, EnumCooldownType, Cooldown>> cooldowns = new CopyOnWriteArrayList<>();

    @Override
    public List<MutableTriple<UUID, EnumCooldownType, Cooldown>> getCooldowns() {
        return cooldowns;
    }

    @Override
    public void addCooldown(UUID uuid, EnumCooldownType type, Cooldown cooldown) {
        this.cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(type));
        this.cooldowns.add(new MutableTriple<>(uuid, type, cooldown));
    }

    @Override
    public Cooldown getCooldown(UUID uuid, EnumCooldownType type) {
        return this.cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(type))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeCooldown(UUID uuid, EnumCooldownType type) {
        this.cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(type));
    }
}