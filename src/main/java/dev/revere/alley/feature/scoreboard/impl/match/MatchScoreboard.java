package dev.revere.alley.feature.scoreboard.impl.match;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.IScoreboard;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboard implements IScoreboard {
    protected final Alley plugin;
    protected final MatchScoreboardFactory matchScoreboardFactory;

    /**
     * Constructor for the MatchScoreboard class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboard(Alley plugin) {
        this.plugin = plugin;
        this.matchScoreboardFactory = new MatchScoreboardFactory(plugin);
    }

    @Override
    public List<String> getLines(Profile profile) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        AbstractMatch match = profile.getMatch();
        List<GameParticipant<MatchGamePlayerImpl>> participants = match.getParticipants();

        GameParticipant<MatchGamePlayerImpl> you = participants.stream()
                .filter(p -> p.getPlayer().getUuid().equals(profile.getUuid()))
                .findFirst().orElse(null);

        GameParticipant<MatchGamePlayerImpl> opponent = participants.stream()
                .filter(p -> !p.getPlayer().getUuid().equals(profile.getUuid()))
                .findFirst().orElse(null);

        if (you == null || opponent == null) return Collections.emptyList();

        return this.matchScoreboardFactory.getMatchType(profile).getLines(profile, player, you, opponent);
    }
}