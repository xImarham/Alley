package dev.revere.alley.provider.scoreboard.impl.match;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.IScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.impl.state.MatchScoreboardEndingImpl;
import dev.revere.alley.provider.scoreboard.impl.match.impl.state.MatchScoreboardStartingImpl;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboard implements IScoreboard {
    private final MatchScoreboardRegistry registry;

    private final MatchScoreboardStartingImpl matchScoreboardStarting;
    private final MatchScoreboardEndingImpl matchScoreboardEnding;

    /**
     * Constructor for the MatchScoreboard class.
     * It instantiates the registry, which automatically discovers all providers.
     */
    public MatchScoreboard() {
        this.registry = new MatchScoreboardRegistry();
        this.registry.initialize();

        this.matchScoreboardStarting = new MatchScoreboardStartingImpl();
        this.matchScoreboardEnding = new MatchScoreboardEndingImpl();
    }

    @Override
    public List<String> getLines(Profile profile) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        AbstractMatch match = profile.getMatch();

        GameParticipant<MatchGamePlayerImpl> you = match.getParticipant(player);
        GameParticipant<MatchGamePlayerImpl> opponent = match.getOpponent(player);
        if (you == null || opponent == null) {
            return Collections.emptyList();
        }

        if (match.getState() == EnumMatchState.STARTING) {
            return matchScoreboardStarting.getLines(profile, player, you, opponent);
        }
        if (match.getState() == EnumMatchState.ENDING_MATCH) {
            return matchScoreboardEnding.getLines(profile, player, you, opponent);
        }

        IMatchScoreboard scoreboardImpl = registry.getScoreboard(match);
        if (scoreboardImpl == null) {
            return Collections.emptyList();
        }

        return scoreboardImpl.getLines(profile, player, you, opponent);
    }
}