package me.emmy.alley.match.impl;

import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;

public class LivesMatchImpl extends RegularMatchImpl {

    /**
     * Constructor for the LivesMatchImpl class.
     *
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public LivesMatchImpl(Kit kit, Arena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(kit, arena, participantA, participantB);
    }
}
