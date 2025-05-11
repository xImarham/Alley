package dev.revere.alley.feature.scoreboard.impl.match.impl.state;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardEndingImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardEndingImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardEndingImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();

        for (String line : this.plugin.getConfigService().getConfig("providers/scoreboard.yml").getStringList("scoreboard.lines.ending")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{opponent}", this.getColoredName(opponent.getPlayer().getPlayer()))
                    .replaceAll("\\{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replaceAll("\\{player-ping}", String.valueOf(this.getPing(player)))
                    .replaceAll("\\{duration}", profile.getMatch().getDuration())
                    .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getKit().getDisplayName())
                    .replaceAll("\\{winner}", opponent.getPlayer().isDead() ? you.getPlayer().getUsername() : opponent.getPlayer().getUsername())
                    .replaceAll("\\{end-result}", opponent.getPlayer().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!"));
        }

        return scoreboardLines;
    }
}