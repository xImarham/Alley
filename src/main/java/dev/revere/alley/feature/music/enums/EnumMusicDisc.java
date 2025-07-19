package dev.revere.alley.feature.music.enums;

import lombok.Getter;
import org.bukkit.Material;

/**
 * Represents a music disc in the game.
 * Each music disc has a material type, a title, and a unique ID.
 * This enum is used to manage and identify different music discs.
 *
 * @author Emmy
 * @project Alley
 * @date 27/10/2024 - 08:43
 */
@Getter
public enum EnumMusicDisc {
    GOLD_RECORD(Material.GOLD_RECORD, "13", 2256),
    GREEN_RECORD(Material.GREEN_RECORD, "Cat", 2257),
    RECORD_3(Material.RECORD_3, "Blocks", 2258),
    RECORD_4(Material.RECORD_4, "Chirp", 2259),
    RECORD_5(Material.RECORD_5, "Far", 2260),
    RECORD_6(Material.RECORD_6, "Mall", 2261),
    RECORD_7(Material.RECORD_7, "Mellohi", 2262),
    RECORD_8(Material.RECORD_8, "Stal", 2263),
    RECORD_9(Material.RECORD_9, "Strad", 2264),
    RECORD_10(Material.RECORD_10, "Ward", 2265),
    RECORD_11(Material.RECORD_11, "11", 2266),
    RECORD_12(Material.RECORD_12, "Wait", 2267);

    private final Material material;
    private final String title;
    private final int id;

    /**
     * Constructor for the MusicDisc enum.
     *
     * @param material The material type of the music disc.
     * @param title    The title of the music disc.
     * @param id       The unique ID of the music.
     */
    EnumMusicDisc(Material material, String title, int id) {
        this.material = material;
        this.title = title;
        this.id = id;
    }
}