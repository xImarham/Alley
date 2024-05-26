package me.emmy.alley.ffa;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.ffa.enums.EnumFFAState;
import me.emmy.alley.kit.Kit;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 14:30
 */
@Getter
@Setter
public class FFA {
    private EnumFFAState ffaState = EnumFFAState.SPAWN;
    private final Arena ffaArena;
    private final Kit ffaKit;

    /**
     * Constructor for the FFA class.
     *
     * @param ffaKit The FFA kit.
     * @param ffaArena The FFA arena.
     */
    public FFA(Kit ffaKit, Arena ffaArena) {
        this.ffaArena = ffaArena;
        this.ffaKit = ffaKit;
    }
}
