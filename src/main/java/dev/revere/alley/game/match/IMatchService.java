package dev.revere.alley.game.match;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.core.lifecycle.IService;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IMatchService extends IService {
    /**
     * Gets a list of all currently active matches.
     * @return An unmodifiable list of active matches.
     */
    List<AbstractMatch> getMatches();

    /**
     * Gets the list of commands that are blocked for players while in a match.
     * @return A list of blocked command strings.
     */
    List<String> getBlockedCommands();

    /**
     * Adds a match to the service's tracking list.
     * Should be called right after a match is created.
     * @param match The match to add.
     */
    void addMatch(AbstractMatch match);

    /**
     * Removes a match from the service's tracking list.
     * Should be called when a match ends.
     * @param match The match to remove.
     */
    void removeMatch(AbstractMatch match);

    /**
     * Creates, starts, and registers a new match with the given parameters.
     *
     * @param kit            The kit to be used in the match.
     * @param arena          The arena where the match will take place.
     * @param participantA   The first participant in the match.
     * @param participantB   The second participant in the match.
     * @param teamMatch      Whether this is a team-based match.
     * @param affectStatistics Whether this match should affect player stats (Elo, wins/losses).
     * @param isRanked       Whether this match is ranked.
     */
    void createAndStartMatch(Kit kit, AbstractArena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, boolean teamMatch, boolean affectStatistics, boolean isRanked);
}