package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
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
public class MatchScoreboardLivesImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardLivesImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardLivesImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.playing.solo.lives-match")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(profile))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{player-lives}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getLives()))
                    .replaceAll("\\{opponent-lives}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getLives()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
