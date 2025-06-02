package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 02/06/2025
 */
public class MatchScoreboardBedImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardBedImpl class.
     *
     * @param plugin The Alley instance
     */
    public MatchScoreboardBedImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        MatchBedImpl bedMatch = (MatchBedImpl) profile.getMatch();

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.playing.solo.bed-match")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(profile))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{player-bed}", ScoreboardUtil.visualizeBed(you.getPlayer().getData().isBedBroken()))
                    .replaceAll("\\{opponent-bed}", ScoreboardUtil.visualizeBed(opponent.getPlayer().getData().isBedBroken()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{color}", String.valueOf(bedMatch.getTeamAColor()))
                    .replaceAll("\\{opponent-color}", String.valueOf(bedMatch.getTeamBColor()))
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
