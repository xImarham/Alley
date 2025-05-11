package dev.revere.alley.feature.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.scoreboard.IScoreboard;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class SpectatorScoreboard implements IScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the SpectatorScoreboard class.
     *
     * @param plugin The Alley plugin instance.
     */
    public SpectatorScoreboard(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile) {
        List<String> scoreboardLines = new ArrayList<>();

        if (profile.getMatch() instanceof MatchRegularImpl) {
            for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.spectating")) {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{playerA}", profile.getMatch().getParticipants().get(0).getPlayer().getUsername())
                        .replaceAll("\\{playerB}", profile.getMatch().getParticipants().get(1).getPlayer().getUsername())
                        .replaceAll("\\{pingA}", String.valueOf(this.getPing(profile.getMatch().getParticipants().get(0).getPlayer().getPlayer())))
                        .replaceAll("\\{pingB}", String.valueOf(this.getPing(profile.getMatch().getParticipants().get(1).getPlayer().getPlayer())))
                        .replaceAll("\\{colorA}", String.valueOf(((MatchRegularImpl) profile.getMatch()).getTeamAColor()))
                        .replaceAll("\\{colorB}", String.valueOf(((MatchRegularImpl) profile.getMatch()).getTeamBColor()))
                        .replaceAll("\\{duration}", profile.getMatch().getDuration())
                        .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                        .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
            }
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}