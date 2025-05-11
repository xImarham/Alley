package dev.revere.alley.feature.scoreboard.impl.match.impl.state;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.animation.type.internal.impl.DotAnimationImpl;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardStartingImpl implements IMatchScoreboard {
    protected final Alley plugin;

    private final DotAnimationImpl dotAnimation;

    /**
     * Constructor for the MatchScoreboardStartingImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardStartingImpl(Alley plugin) {
        this.plugin = plugin;

        this.dotAnimation = new DotAnimationImpl();
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.starting")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{player}", this.getColoredName(player))
                    .replaceAll("\\{opponent}", this.getColoredName(opponent.getPlayer().getPlayer()))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{dot-animation}", this.dotAnimation.getCurrentFrame())
                    .replaceAll("\\{countdown}", String.valueOf(profile.getMatch().getRunnable().getStage()))
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
