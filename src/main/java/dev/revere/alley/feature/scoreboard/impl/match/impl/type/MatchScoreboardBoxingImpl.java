package dev.revere.alley.feature.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardBoxingImpl implements IMatchScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the MatchScoreboardBoxingImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchScoreboardBoxingImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> lines = new ArrayList<>();

        FileConfiguration config = plugin.getConfigService().getScoreboardConfig();
        List<String> template = config.getStringList("scoreboard.lines.playing.boxing-match");

        int playerHits = profile.getMatch().getGamePlayer(player).getData().getHits();
        int opponentHits = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getHits();

        int playerCombo = profile.getMatch().getGamePlayer(player).getData().getCombo();
        int opponentCombo = profile.getMatch().getGamePlayer(opponent.getPlayer().getPlayer()).getData().getCombo();

        String hitDifference = this.formatHitDifference(playerHits, opponentHits, config);
        String combo = this.formatCombo(playerCombo, opponentCombo, config);

        for (String line : template) {
            lines.add(CC.translate(line)
                    .replace("{opponent}", this.getColoredName(opponent.getPlayer().getPlayer()))
                    .replace("{opponent-ping}", String.valueOf(this.getPing(opponent.getPlayer().getPlayer())))
                    .replace("{player-ping}", String.valueOf(this.getPing(player)))
                    .replace("{player-hits}", String.valueOf(playerHits))
                    .replace("{opponent-hits}", String.valueOf(opponentHits))
                    .replace("{difference}", hitDifference)
                    .replace("{combo}", combo)
                    .replace("{duration}", profile.getMatch().getDuration())
                    .replace("{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replace("{kit}", profile.getMatch().getKit().getDisplayName())
            );
        }

        return lines;
    }

    /**
     * Format the hit difference between the player and opponent.
     *
     * @param playerHits   the number of hits the player has made.
     * @param opponentHits the number of hits the opponent has made.
     * @param config       the configuration file.
     * @return the formatted hit difference string.
     */
    private String formatHitDifference(int playerHits, int opponentHits, FileConfiguration config) {
        int difference = playerHits - opponentHits;

        String format;

        if (difference > 0) {
            format = config.getString("boxing-hit-difference.positive-difference", "&a(+{difference})").replace("{difference}", String.valueOf(difference));
        } else if (difference < 0) {
            format = config.getString("boxing-hit-difference.negative-difference", "&c(-{difference})").replace("{difference}", String.valueOf(-difference));
        } else {
            format = config.getString("boxing-hit-difference.no-difference", "&a(+0)");
        }

        return CC.translate(format);
    }

    /**
     * Format the combo display for the player and opponent.
     *
     * @param playerCombo   the player's combo count.
     * @param opponentCombo the opponent's combo count.
     * @param config        the configuration file.
     * @return the formatted combo string.
     */
    private String formatCombo(int playerCombo, int opponentCombo, FileConfiguration config) {
        String format;

        if (playerCombo > 1) {
            format = config.getString("boxing-combo-display.positive-combo", "&a{combo} Combo").replace("{combo}", String.valueOf(playerCombo));
        } else if (opponentCombo > 1) {
            format = config.getString("boxing-combo-display.negative-combo", "&c{combo} Combo").replace("{combo}", String.valueOf(opponentCombo));
        } else {
            format = config.getString("boxing-combo-display.no-combo", "&fNo Combo");
        }

        return CC.translate(format);
    }
}