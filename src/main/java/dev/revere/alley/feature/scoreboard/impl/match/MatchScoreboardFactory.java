package dev.revere.alley.feature.scoreboard.impl.match;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.setting.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingBoxingImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingLivesImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingStickFightImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.type.MatchScoreboardBoxingImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.state.MatchScoreboardEndingImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.MatchScoreboardRegularImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.state.MatchScoreboardStartingImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.type.MatchScoreboardLivesImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.type.MatchScoreboardRoundsImpl;
import dev.revere.alley.feature.scoreboard.impl.match.impl.type.MatchScoreboardStickFightImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.profile.Profile;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardFactory {
    protected final Alley plugin;

    private final MatchScoreboardStartingImpl matchScoreboardStarting;
    private final MatchScoreboardEndingImpl matchScoreboardEnding;

    private final MatchScoreboardBoxingImpl matchScoreboardBoxing;
    private final MatchScoreboardRoundsImpl matchScoreboardRounds;
    private final MatchScoreboardStickFightImpl matchScoreboardStickFight;
    private final MatchScoreboardLivesImpl matchScoreboardLives;

    private final MatchScoreboardRegularImpl matchScoreboardRegular;

    /**
     * Constructor for the MatchScoreboardFactory class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardFactory(Alley plugin) {
        this.plugin = plugin;

        this.matchScoreboardStarting = new MatchScoreboardStartingImpl(this.plugin);
        this.matchScoreboardEnding = new MatchScoreboardEndingImpl(this.plugin);

        this.matchScoreboardBoxing = new MatchScoreboardBoxingImpl(this.plugin);
        this.matchScoreboardRounds = new MatchScoreboardRoundsImpl(this.plugin);
        this.matchScoreboardStickFight = new MatchScoreboardStickFightImpl(this.plugin);
        this.matchScoreboardLives = new MatchScoreboardLivesImpl(this.plugin);

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
        if (match.getKit().isSettingEnabled(KitSettingBattleRushImpl.class)) return this.matchScoreboardRounds;
        if (match.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) return this.matchScoreboardStickFight;
        if (match.getKit().isSettingEnabled(KitSettingLivesImpl.class)) return this.matchScoreboardLives;

        return this.matchScoreboardRegular;
    }
}