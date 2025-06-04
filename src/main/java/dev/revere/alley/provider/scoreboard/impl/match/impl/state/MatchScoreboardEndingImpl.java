package dev.revere.alley.provider.scoreboard.impl.match.impl.state;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
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

        AbstractMatch match = profile.getMatch();
        if (match == null) return scoreboardLines;

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.ending")) {
            scoreboardLines.add(CC.translate(line)
                    .replace("{opponent}", opponent.getPlayer().getUsername())
                    .replace("{duration}", match.getDuration())
                    .replace("{arena}", String.valueOf(match.getArena()))
                    .replace("{kit}", String.valueOf(match.getKit()))
                    .replace("{winner}", opponent.getPlayer().isDead() ? you.getPlayer().getUsername() : opponent.getPlayer().getUsername())
                    .replace("{end-result}", opponent.getPlayer().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!"));
        }

        return scoreboardLines;
    }
}