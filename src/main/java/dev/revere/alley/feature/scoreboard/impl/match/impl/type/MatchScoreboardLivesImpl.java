package dev.revere.alley.feature.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.reflection.BukkitReflection;
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

        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.playing.lives-match")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(opponent.getPlayer().getPlayer()))
                    .replaceAll("\\{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                    .replaceAll("\\{player-lives}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getLives()))
                    .replaceAll("\\{opponent-lives}", String.valueOf(profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getLives()))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
