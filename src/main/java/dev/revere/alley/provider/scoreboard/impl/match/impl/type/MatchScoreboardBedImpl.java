package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.AbstractMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(match = MatchBedImpl.class)
public class MatchScoreboardBedImpl extends AbstractMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.bed-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.bed-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        MatchRegularImpl match = (MatchRegularImpl) profile.getMatch();

        String yourColorDisplay = getTeamDisplay(match.getTeamColor(you));
        String opponentColorDisplay = getTeamDisplay(match.getTeamColor(opponent));

        return baseLine
                .replace("{your-bed}", ScoreboardUtil.visualizeBed(you.isBedBroken()))
                .replace("{opponent-bed}", ScoreboardUtil.visualizeBed(opponent.isBedBroken()))
                .replace("{your-color-display}", yourColorDisplay)
                .replace("{opponent-color-display}", opponentColorDisplay);
    }

    private String getTeamDisplay(ChatColor teamColor) {
        if (teamColor == null) return "";
        String colorName = teamColor.name().toLowerCase();
        return Alley.getInstance().getService(IConfigService.class).getScoreboardConfig().getString("scoreboard.team-displays." + colorName, "");
    }
}
