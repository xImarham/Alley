package me.emmy.alley.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 17:11
 */
public class SoundUtil {
    public static void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 20F, 0.1F);

    }

    public static void playSuccess(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 20F, 15F);
    }

    public static void playerClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.CLICK, 20F, 15F);
    }

    public static void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 20F, 15F);
    }

    public static void playerHightoneExplode(Player player) {
        player.playSound(player.getLocation(), Sound.EXPLODE, 2.0F, 1.5F);
    }
}
