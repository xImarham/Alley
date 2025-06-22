package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.ChatColor;
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

        GameParticipant<MatchGamePlayerImpl> yourTeam;
        GameParticipant<MatchGamePlayerImpl> opponentTeam;
        String yourColorDisplay;
        String opponentColorDisplay;

        if (bedMatch.getParticipantA().getPlayers().contains(you.getPlayer())) {
            yourTeam = bedMatch.getParticipantA();
            opponentTeam = bedMatch.getParticipantB();
            yourColorDisplay = getTeamDisplay(bedMatch.getTeamAColor());
            opponentColorDisplay = getTeamDisplay(bedMatch.getTeamBColor());
        } else {
            yourTeam = bedMatch.getParticipantB();
            opponentTeam = bedMatch.getParticipantA();
            yourColorDisplay = getTeamDisplay(bedMatch.getTeamBColor());
            opponentColorDisplay = getTeamDisplay(bedMatch.getTeamAColor());
        }

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.playing.solo.bed-match")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(this.plugin.getProfileService().getProfile(opponent.getPlayer().getUuid())))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{your-bed}", ScoreboardUtil.visualizeBed(yourTeam.isBedBroken()))
                    .replaceAll("\\{opponent-bed}", ScoreboardUtil.visualizeBed(opponentTeam.isBedBroken()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{your-color-display}", yourColorDisplay)
                    .replaceAll("\\{opponent-color-display}", opponentColorDisplay)
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }

    /**
     * Get team display from config based on team color
     * @param teamColor The team color enum
     * @return Display string from config like "&c[R]" or "&9[BLUE]"
     */
    private String getTeamDisplay(ChatColor teamColor) {
        String realColorName = teamColor.name().toLowerCase();
        String configPath = "scoreboard.team-displays." + realColorName;

        return this.plugin.getConfigService().getScoreboardConfig().getString(configPath);
    }
}
