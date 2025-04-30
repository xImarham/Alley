package dev.revere.alley.feature.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.reflection.BukkitReflection;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardRoundsImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardRoundsImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardRoundsImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        MatchRoundsImpl roundsMatch = (MatchRoundsImpl) profile.getMatch();

        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.battlerush-match")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(opponent.getPlayer().getPlayer()))
                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                    .replaceAll("\\{time-left}", this.getFormattedTime(profile))
                    .replaceAll("\\{goals}", ScoreboardUtil.visualizeGoalsAsCircles(roundsMatch.getParticipantA().getPlayer().getData().getGoals(), 3))
                    .replaceAll("\\{opponent-goals}", ScoreboardUtil.visualizeGoalsAsCircles(roundsMatch.getParticipantB().getPlayer().getData().getGoals(), 3))
                    .replaceAll("\\{kills}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getKills()))
                    .replaceAll("\\{current-round}", String.valueOf(roundsMatch.getCurrentRound()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{color}", String.valueOf(roundsMatch.getTeamAColor()))
                    .replaceAll("\\{opponent-color}", String.valueOf(roundsMatch.getTeamBColor()))
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }

    /**
     * Get the formatted time left in the match.
     *
     * @param profile The profile of the player.
     * @return The formatted time left in the match.
     */
    private @NotNull String getFormattedTime(Profile profile) {
        long elapsedTime = System.currentTimeMillis() - profile.getMatch().getStartTime();
        long remainingTime = Math.max(900_000 - elapsedTime, 0);
        return TimeUtil.millisToFourDigitSecondsTimer(remainingTime);
    }
}