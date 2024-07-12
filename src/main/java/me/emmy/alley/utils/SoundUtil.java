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
    public void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 20F, 0.1F);

    }

    public void playSuccess(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 20F, 15F);
    }

    public void playerClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.CLICK, 20F, 15F);
    }

    public void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 20F, 15F);
    }

    public void playerHightoneExplode(Player player) {
        player.playSound(player.getLocation(), Sound.EXPLODE, 2.0F, 1.5F);
    }
}
