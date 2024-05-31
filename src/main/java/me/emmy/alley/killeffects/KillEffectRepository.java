package me.emmy.alley.killeffects;

import lombok.Getter;
import me.emmy.alley.killeffects.impl.NoneKillEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zion
 * @project Alley
 * @date 01/06/2024
 */
public class KillEffectRepository {

    @Getter
    private final List<AbstractKillEffect> killEffects = new ArrayList<>();

    public KillEffectRepository() {
        killEffects.add(new NoneKillEffect());
    }
}
