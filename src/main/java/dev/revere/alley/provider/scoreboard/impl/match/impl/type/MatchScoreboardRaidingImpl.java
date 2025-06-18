package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

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
 * @since 16/06/2025
 */
public class MatchScoreboardRaidingImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardRaidingImpl class.
     *
     * @param plugin The Alley plugin instance
     */
    public MatchScoreboardRaidingImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        MatchRegularImpl regularMatch = (MatchRegularImpl) profile.getMatch();
        boolean isTeamMatch = regularMatch.isTeamMatch();
        String configPath = isTeamMatch ? "scoreboard.lines.playing.team.raiding-match" : "scoreboard.lines.playing.solo.raiding-match";

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList(configPath)) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(this.plugin.getProfileService().getProfile(opponent.getPlayer().getUuid())))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{duration}", regularMatch.getDuration())
                    .replaceAll("\\{arena}", regularMatch.getArena().getDisplayName() == null ? "&c&lNULL" : regularMatch.getArena().getDisplayName())
                    .replaceAll("\\{kit}", regularMatch.getKit().getDisplayName())
                    .replaceAll("\\{role}", you.getPlayer().getData().getRole().getDisplayName())
                    .replaceAll("\\{playersA}", String.valueOf(regularMatch.getParticipantA().getPlayerSize()))
                    .replaceAll("\\{playersB}", String.valueOf(regularMatch.getParticipantB().getPlayerSize()))
                    .replaceAll("\\{aliveA}", String.valueOf(regularMatch.getParticipantA().getAlivePlayerSize()))
                    .replaceAll("\\{aliveB}", String.valueOf(regularMatch.getParticipantB().getAlivePlayerSize()))
            );
        }

        return scoreboardLines;
    }
}
