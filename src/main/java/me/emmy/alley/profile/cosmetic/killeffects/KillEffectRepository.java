package me.emmy.alley.profile.cosmetic.killeffects;

import lombok.Getter;
import me.emmy.alley.profile.cosmetic.killeffects.impl.NoneKillEffect;
import me.emmy.alley.profile.cosmetic.killeffects.impl.ThunderKillEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zion
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class KillEffectRepository {
    private final List<AbstractKillEffect> killEffects = new ArrayList<>();

    /**
     * Constructor to register all kill effects.
     */
    public KillEffectRepository() {
        registerKillEffect(NoneKillEffect.class);
        registerKillEffect(ThunderKillEffect.class);
    }

    /**
     * Method to register a kill effect class.
     *
     * @param clazz The kill effect class.
     */
    private void registerKillEffect(Class<? extends AbstractKillEffect> clazz) {
        try {
            AbstractKillEffect instance = clazz.getDeclaredConstructor().newInstance();
            killEffects.add(instance);
        } catch (Exception e) {
            System.out.println("Failed to register kill effect class " + clazz.getSimpleName() + "!");
        }
    }

    public AbstractKillEffect getByName(String name) {
        for(AbstractKillEffect killEffect : killEffects) {
            if(killEffect.getName().equals(name)) {
                return killEffect;
            }
        }

        return null;
    }
}
