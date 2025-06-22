package dev.revere.alley.provider.scoreboard.impl.match.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardRegularImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardRegularImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardRegularImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        MatchRegularImpl regularMatch = (MatchRegularImpl) profile.getMatch();
        boolean isTeamMatch = regularMatch.isTeamMatch();
        String configPath = isTeamMatch ? "scoreboard.lines.playing.team.regular-match" : "scoreboard.lines.playing.solo.regular-match";

        GameParticipant<MatchGamePlayerImpl> yourTeam = determinePlayerTeam(regularMatch, you);
        GameParticipant<MatchGamePlayerImpl> opponentTeam = (yourTeam == regularMatch.getParticipantA())
                ? regularMatch.getParticipantB()
                : regularMatch.getParticipantA();

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList(configPath)) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(this.plugin.getProfileService().getProfile(opponent.getPlayer().getUuid())))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{duration}", regularMatch.getDuration())
                    .replaceAll("\\{arena}", regularMatch.getArena().getDisplayName() == null ? "&c&lNULL" : regularMatch.getArena().getDisplayName())
                    .replaceAll("\\{kit}", regularMatch.getKit().getDisplayName())
                    .replaceAll("\\{your-players}", String.valueOf(yourTeam.getPlayerSize()))
                    .replaceAll("\\{opponent-players}", String.valueOf(opponentTeam.getPlayerSize()))
                    .replaceAll("\\{your-alive}", String.valueOf(yourTeam.getAlivePlayerSize()))
                    .replaceAll("\\{opponent-alive}", String.valueOf(opponentTeam.getAlivePlayerSize()))
            );
        }

        return scoreboardLines;
    }

    /**
     * Determines which participant object represents the player's team.
     *
     * @param match The current match.
     * @param playerParticipant The participant object for the player.
     * @return The GameParticipant corresponding to the player's team.
     */
    private GameParticipant<MatchGamePlayerImpl> determinePlayerTeam(MatchRegularImpl match, GameParticipant<MatchGamePlayerImpl> playerParticipant) {
        boolean isInParticipantA = match.getParticipantA().getPlayers().contains(playerParticipant.getPlayer());
        return isInParticipantA ? match.getParticipantA() : match.getParticipantB();
    }
}