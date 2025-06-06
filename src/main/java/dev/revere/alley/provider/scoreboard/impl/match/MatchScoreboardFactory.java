package dev.revere.alley.provider.scoreboard.impl.match;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.impl.MatchLivesImpl;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.impl.MatchScoreboardRegularImpl;
import dev.revere.alley.provider.scoreboard.impl.match.impl.state.MatchScoreboardEndingImpl;
import dev.revere.alley.provider.scoreboard.impl.match.impl.state.MatchScoreboardStartingImpl;
import dev.revere.alley.provider.scoreboard.impl.match.impl.type.*;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardFactory {
    protected final Alley plugin;

    protected final MatchScoreboardStartingImpl matchScoreboardStarting;
    protected final MatchScoreboardEndingImpl matchScoreboardEnding;

    protected final MatchScoreboardStickFightImpl matchScoreboardStickFight;
    protected final MatchScoreboardBoxingImpl matchScoreboardBoxing;

    protected final MatchScoreboardRoundsImpl matchScoreboardRounds;
    protected final MatchScoreboardLivesImpl matchScoreboardLives;
    protected final MatchScoreboardBedImpl matchScoreboardBed;

    protected final MatchScoreboardRegularImpl matchScoreboardRegular;

    /**
     * Constructor for the MatchScoreboardFactory class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardFactory(Alley plugin) {
        this.plugin = plugin;

        this.matchScoreboardStarting = new MatchScoreboardStartingImpl(this.plugin);
        this.matchScoreboardEnding = new MatchScoreboardEndingImpl(this.plugin);

        this.matchScoreboardStickFight = new MatchScoreboardStickFightImpl(this.plugin);
        this.matchScoreboardBoxing = new MatchScoreboardBoxingImpl(this.plugin);

        this.matchScoreboardRounds = new MatchScoreboardRoundsImpl(this.plugin);
        this.matchScoreboardLives = new MatchScoreboardLivesImpl(this.plugin);
        this.matchScoreboardBed = new MatchScoreboardBedImpl(this.plugin);

        this.matchScoreboardRegular = new MatchScoreboardRegularImpl(this.plugin);
    }

    /**
     * Gets the match type scoreboard renderer based on the match state and kit settings.
     *
     * @param profile The profile of the player.
     * @return The appropriate scoreboard renderer for the match type.
     */
    public IMatchScoreboard getMatchType(Profile profile) {
        AbstractMatch match = profile.getMatch();

        if (match.getState() == EnumMatchState.STARTING) return this.matchScoreboardStarting;
        if (match.getState() == EnumMatchState.ENDING_MATCH) return this.matchScoreboardEnding;

        if (match.getKit().isSettingEnabled(KitSettingBoxingImpl.class)) return this.matchScoreboardBoxing;
        if (match.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) return this.matchScoreboardStickFight;

        if (match instanceof MatchRoundsImpl) return this.matchScoreboardRounds;
        if (match instanceof MatchLivesImpl) return this.matchScoreboardLives;
        if (match instanceof MatchBedImpl) return this.matchScoreboardBed;

        return this.matchScoreboardRegular;
    }
}