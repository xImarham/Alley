package me.emmy.alley.match.impl;

import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.AbstractMatch;

public class LivesMatchImpl extends AbstractMatch {

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param kit   The kit of the match.
     * @param arena The arena of the match.
     */
    public LivesMatchImpl(Kit kit, Arena arena) {
        super(kit, arena);
    }

    @Override
    public boolean canEndRound() {
        return false;
    }

    @Override
    public boolean canEndMatch() {
        return false;
    }
}
