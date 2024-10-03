package me.emmy.alley.match.impl.party;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.impl.MatchRegularImpl;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.queue.Queue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Emmy
 * @project Alley
 * @date 03/10/2024 - 16:05
 */
@Getter
@Setter
public class MatchPartyRegularImpl extends AbstractMatch {

    private final List<GameParticipant<MatchGamePlayerImpl>> redTeam;
    private final List<GameParticipant<MatchGamePlayerImpl>> blueTeam;

    private List<GameParticipant<MatchGamePlayerImpl>> winners;
    private List<GameParticipant<MatchGamePlayerImpl>> losers;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param matchQueue The queue of the match.
     * @param matchKit   The kit of the match.
     * @param matchArena The matchArena of the match.
     */
    public MatchPartyRegularImpl(Queue matchQueue, Kit matchKit, Arena matchArena, List<GameParticipant<MatchGamePlayerImpl>> redTeam, List<GameParticipant<MatchGamePlayerImpl>> blueTeam) {
        super(matchQueue, matchKit, matchArena, false);
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Stream.concat(redTeam.stream(), blueTeam.stream()).collect(Collectors.toList());
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return false;
    }

    @Override
    public boolean canEndMatch() {
        //return redTeam.stream().allMatch(GameParticipant::isAllDead) || blueTeam.stream().allMatch(GameParticipant::isAllDead);
        return false;
    }
}
