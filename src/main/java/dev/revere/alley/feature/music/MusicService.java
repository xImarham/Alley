package dev.revere.alley.feature.music;

import dev.revere.alley.feature.music.enums.EnumMusicDisc;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
@Getter
@Service(provides = IMusicService.class, priority = 175)
public class MusicService implements IMusicService {
    private final IProfileService profileService;

    private List<EnumMusicDisc> musicDiscs = new ArrayList<>();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * DI Constructor for the MusicService class.
     *
     * @param profileService The profile service to be used by this music service.
     */
    public MusicService(IProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.musicDiscs = Arrays.asList(EnumMusicDisc.values());
    }

    /**
     * Returns a random music disc from the EnumMusicDisc enum.
     *
     * @return A random EnumMusicDisc value.
     */
    @Override
    public EnumMusicDisc getRandomMusicDisc() {
        EnumMusicDisc[] values = EnumMusicDisc.values();
        return values[this.random.nextInt(values.length)];
    }

    @Override
    public Set<EnumMusicDisc> getSelectedMusicDiscs(Profile profile) {
        return profile.getProfileData().getMusicData().getSelectedDiscs().stream().map(name -> {
            try {
                return EnumMusicDisc.valueOf(name);
            } catch (IllegalArgumentException exception) {
                Logger.logException("Invalid music disc name: " + name, exception);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public EnumMusicDisc getRandomSelectedMusicDisc(Profile profile) {
        List<EnumMusicDisc> selectedDiscs = new ArrayList<>(this.getSelectedMusicDiscs(profile));

        if (selectedDiscs.isEmpty()) {
            Logger.error("No music discs selected for profile: " + profile.getUuid());
            return this.getRandomMusicDisc();
        }

        return selectedDiscs.get(this.random.nextInt(selectedDiscs.size()));
    }
}