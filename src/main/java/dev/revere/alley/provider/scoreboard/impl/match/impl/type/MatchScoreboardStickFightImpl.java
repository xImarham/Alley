package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.game.match.impl.kit.MatchStickFightImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardStickFightImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardStickFightImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardStickFightImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        MatchStickFightImpl stickFightMatch = (MatchStickFightImpl) profile.getMatch();

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.playing.solo.stickfight-match")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(profile))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{goals}", ScoreboardUtil.visualizeGoals(stickFightMatch.getParticipantA().getPlayer().getData().getGoals(), 5))
                    .replaceAll("\\{opponent-goals}", ScoreboardUtil.visualizeGoals(stickFightMatch.getParticipantB().getPlayer().getData().getGoals(), 5))
                    .replaceAll("\\{current-round}", String.valueOf(stickFightMatch.getCurrentRound()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{color}", String.valueOf(stickFightMatch.getTeamAColor()))
                    .replaceAll("\\{opponent-color}", String.valueOf(stickFightMatch.getTeamBColor()))
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}