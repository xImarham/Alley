package dev.revere.alley.game.match.data;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
public abstract class AbstractMatchData {
    private final String kit;
    private final String arena;

    /**
     * Constructor for the AbstractMatchData class.
     *
     * @param kit   The kit used in the match.
     * @param arena The arena where the match took place.
     */
    public AbstractMatchData(String kit, String arena) {
        this.kit = kit;
        this.arena = arena;
    }
}