package dev.revere.alley.profile.data.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.music.IMusicService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
@Getter
@Setter
public class ProfileMusicData {
    private final Set<String> selectedDiscs;
    private boolean isPlaying = false;

    public ProfileMusicData () {
        this.selectedDiscs = new HashSet<>();

        IMusicService musicService = Alley.getInstance().getService(IMusicService.class);
        musicService.getMusicDiscs().forEach(disc -> this.addDisc(disc.name()));
    }

    /**
     * Adds a music disc to the selected discs set.
     *
     * @param disc The name of the music disc to add.
     */
    public void addDisc(String disc) {
        this.selectedDiscs.add(disc);
    }

    /**
     * Removes a music disc from the selected discs set.
     *
     * @param disc The name of the music disc to remove.
     */
    public void removeDisc(String disc) {
        this.selectedDiscs.remove(disc);
    }

    /**
     * Checks if a music disc is selected.
     *
     * @param disc The name of the music disc to check.
     * @return True if the disc is selected, false otherwise.
     */
    public boolean isDiscSelected(String disc) {
        return this.selectedDiscs.contains(disc);
    }
}