package dev.revere.alley.provider.scoreboard.impl.match.annotation;

import dev.revere.alley.base.kit.setting.KitSetting;
import dev.revere.alley.game.match.AbstractMatch;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScoreboardData {
    Class<? extends AbstractMatch> match() default AbstractMatch.class;
    Class<? extends KitSetting> kit() default KitSetting.class;
    boolean isDefault() default false;
}