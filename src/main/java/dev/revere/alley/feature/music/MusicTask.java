package dev.revere.alley.feature.music;

import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/07/2025
 */
@RequiredArgsConstructor
public class MusicTask extends BukkitRunnable {
    private final Player player;
    private final MusicService musicService;
    private final IProfileService profileService;

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        MusicSession session = musicService.getSession(player.getUniqueId());
        if (session == null || session.getTask().getTaskId() != getTaskId()) {
            cancel();
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            musicService.stopMusic(player);
            return;
        }

        boolean shouldPlay = shouldPlayMusic(profile);

        if (!shouldPlay && !session.isPaused()) {
            musicService.sendStopSoundPacket(player, session.getJukeboxLocation());
            session.setPaused(true);
            return;
        }

        if (shouldPlay && session.isPaused()) {
            musicService.sendPlaySoundPacket(player, session.getDisc(), session.getJukeboxLocation());
            session.setPaused(false);
            session.setElapsedSeconds(0);
        }

        if (shouldPlay && !session.isPaused()) {
            if (session.isFinished()) {
                cancel();
                musicService.startMusic(player);
            }

            session.setElapsedSeconds(session.getElapsedSeconds() + 1);
        }
    }

    private boolean shouldPlayMusic(Profile profile) {
        boolean inLobby = profile.getState() == EnumProfileState.LOBBY || profile.getState() == EnumProfileState.WAITING;
        return inLobby && profile.getProfileData().getSettingData().isLobbyMusicEnabled();
    }
}