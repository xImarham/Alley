package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.provider.scoreboard.IScoreboard;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class QueueScoreboard implements IScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the QueueScoreboard class.
     *
     * @param plugin The Alley plugin instance.
     */
    public QueueScoreboard(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile) {
        List<String> scoreboardLines = new ArrayList<>();

        for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.waiting")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replaceAll("\\{playing}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                    .replaceAll("\\{in-queue}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()))
                    .replaceAll("\\{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                    .replaceAll("\\{queued-type}", profile.getQueueProfile().getQueue().getQueueType())
                    .replaceAll("\\{level}", String.valueOf(this.plugin.getLevelService().getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName()))
                    .replaceAll("\\{queued-time}", profile.getQueueProfile().getFormattedElapsedTime())
                    .replaceAll("\\{dot-animation}", this.getDotAnimation().getCurrentFrame())
                    .replaceAll("\\{queued-kit}", profile.getQueueProfile().getQueue().getKit().getName())
            );
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}