package dev.revere.alley.feature.music;

import dev.revere.alley.feature.music.enums.EnumMusicDisc;
import dev.revere.alley.plugin.lifecycle.IService;
import dev.revere.alley.profile.Profile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public interface IMusicService extends IService {
    /**
     * Retrieves a list of all available music discs.
     *
     * @return A list of EnumMusicDisc representing all available music discs.
     */
    List<EnumMusicDisc> getMusicDiscs();

    /**
     * Returns a random music disc from the EnumMusicDisc enum.
     *
     * @return A random EnumMusicDisc value.
     */
    EnumMusicDisc getRandomMusicDisc();

    /**
     * Retrieves the set of music discs currently selected by the player.
     *
     * @param profile The player's profile.
     * @return A set of EnumMusicDisc values representing the selected discs.
     */
    Set<EnumMusicDisc> getSelectedMusicDiscs(Profile profile);

    /**
     * Returns a random music disc from the player's *selected* music discs.
     * If no discs are selected, or if the selection is empty, it might return null or a default disc.
     *
     * @param profile The player's profile.
     * @return A random EnumMusicDisc from the player's selection, or null if no discs are selected.
     */
    EnumMusicDisc getRandomSelectedMusicDisc(Profile profile);
}