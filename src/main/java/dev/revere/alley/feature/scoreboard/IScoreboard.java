package dev.revere.alley.feature.scoreboard;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.animation.enums.EnumAnimationType;
import dev.revere.alley.tool.animation.type.internal.impl.DotAnimationImpl;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public interface IScoreboard {
    /**
     * Gets the scoreboard lines for the given profile.
     *
     * @param profile The profile to get the scoreboard lines for.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile);

    /**
     * Gets the scoreboard lines for the given profile.
     *
     * @param profile The profile to get the scoreboard lines for.
     * @param player  The player to get the scoreboard lines for.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile, Player player);

    /**
     * Gets the dot animation for the scoreboard.
     *
     * @return The dot animation.
     */
    default DotAnimationImpl getDotAnimation() {
        return Alley.getInstance().getAnimationRepository().getAnimation(DotAnimationImpl.class, EnumAnimationType.INTERNAL);
    }
}