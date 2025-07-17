package dev.revere.alley.provider.scoreboard.impl.match.impl.state;

import dev.revere.alley.Alley;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
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
    private final DotAnimationImpl dotAnimation;

    /**
     * Constructor for the MatchScoreboardStartingImpl class.
     */
    public MatchScoreboardStartingImpl() {
        this.dotAnimation = new DotAnimationImpl();
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        IConfigService configService = Alley.getInstance().getService(IConfigService.class);
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = configService.getScoreboardConfig().getStringList("scoreboard.lines.starting");

        for (String line : template) {
            scoreboardLines.add(CC.translate(line)
                    .replace("{opponent}", this.getColoredName(profileService.getProfile(opponent.getLeader().getUuid())))
                    .replace("{opponent-ping}", String.valueOf(this.getPing(opponent.getLeader().getTeamPlayer())))
                    .replace("{player-ping}", String.valueOf(this.getPing(player)))
                    .replace("{duration}", profile.getMatch().getDuration())
                    .replace("{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replace("{dot-animation}", this.dotAnimation.getCurrentFrame())
                    .replace("{countdown}", String.valueOf(profile.getMatch().getRunnable().getStage()))
                    .replace("{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
