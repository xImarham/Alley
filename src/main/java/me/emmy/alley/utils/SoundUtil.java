package me.emmy.alley.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 17:11
 */
@UtilityClass
public class SoundUtil {

    /**
     * Play a custom sound to the player
     *
     * @param player the player to play the sound to
     * @param sound the sound to play
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public void playCustomSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a fail sound to the player (Note Bass)
     *
     * @param player the player to play the sound to
     */
    public void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 20F, 0.1F);

    }

    /**
     * Play a success sound to the player (Note Pling)
     *
     * @param player the player to play the sound to
     */
    public void playSuccess(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 20F, 15F);
    }

    /**
     * Play a click sound to the player (Click)
     *
     * @param player the player to play the sound to
     */
    public void playerClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.CLICK, 20F, 15F);
    }

    /**
     * Play a neutral sound to the player (Note Sticks)
     *
     * @param player the player to play the sound to
     */
    public void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 20F, 15F);
    }

    /**
     * Play a high tone sound to the player (Explode)
     *
     * @param player the player to play the sound to
     */
    public void playerHightoneExplode(Player player) {
        player.playSound(player.getLocation(), Sound.EXPLODE, 2.0F, 1.5F);
    }
}
