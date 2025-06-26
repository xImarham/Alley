package dev.revere.alley.provider.scoreboard.impl.match.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.AbstractMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
@ScoreboardData(isDefault = true)
public class MatchScoreboardRegularImpl extends AbstractMatchScoreboard {
    public MatchScoreboardRegularImpl(Alley plugin) {
        super(plugin);
    }

    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.regular-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.regular-match";
    }
}