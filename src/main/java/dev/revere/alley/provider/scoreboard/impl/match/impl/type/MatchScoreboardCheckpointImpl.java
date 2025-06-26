package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.impl.MatchCheckpointImpl;
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
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(match = MatchCheckpointImpl.class)
public class MatchScoreboardCheckpointImpl extends AbstractMatchScoreboard {
    public MatchScoreboardCheckpointImpl(Alley plugin) {
        super(plugin);
    }

    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.checkpoint-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.checkpoint-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        MatchCheckpointImpl match = (MatchCheckpointImpl) profile.getMatch();
        MatchGamePlayerImpl matchGamePlayer = match.getGamePlayer(player);

        String checkpoints = String.valueOf(matchGamePlayer.getCheckpointCount());

        return baseLine.replace("{checkpoints}", checkpoints);
    }
}