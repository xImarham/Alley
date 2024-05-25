package me.emmy.alley.ffa;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.ffa.enums.EnumFFAState;
import me.emmy.alley.kit.Kit;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 14:30
 */

public class FFA {
    private EnumFFAState ffaState = EnumFFAState.FIGHTING;
    private final Arena ffaArena;
    private final Kit ffaKit;

    /**
     * Constructor for the FFA class.
     *
     * @param ffaKit   The kit of the match.
     * @param ffaArena The matchArena of the match.
     */
    public FFA(Kit ffaKit, Arena ffaArena) {
        this.ffaArena = ffaArena;
        this.ffaKit = ffaKit;
    }

    public void joinFFA() {
        ffaState = EnumFFAState.SPAWN;
    }

    public void startFighting() {
        ffaState = EnumFFAState.FIGHTING;
    }
    public void respawn() {
        ffaState = EnumFFAState.SPAWN;
    }

    public void enterSpawn() {
        ffaState = EnumFFAState.SPAWN;
    }
}
