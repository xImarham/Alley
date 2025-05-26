package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.provider.scoreboard.IScoreboard;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
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
public class LobbyScoreboard implements IScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the LobbyScoreboard class.
     *
     * @param plugin The Alley plugin instance.
     */
    public LobbyScoreboard(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines(Profile profile) {
        List<String> scoreboardLines = new ArrayList<>();

        if (profile.getParty() != null) {
            for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.party")) {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{online}", String.valueOf(this.plugin.getServer().getOnlinePlayers().size()))
                        .replaceAll("\\{level}", String.valueOf(this.plugin.getLevelService().getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName()))
                        .replaceAll("\\{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                        .replaceAll("\\{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                        .replaceAll("\\{party-leader}", profile.getParty().getLeader().getName())
                );
            }
        } else {
            for (String line : this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.lobby")) {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replaceAll("\\{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                        .replaceAll("\\{playing}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.PLAYING).count()))
                        .replaceAll("\\{level}", String.valueOf(this.plugin.getLevelService().getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName()))
                        .replaceAll("\\{in-queue}", String.valueOf(this.plugin.getProfileService().getProfiles().values().stream().filter(profile1 -> profile1.getState() == EnumProfileState.WAITING).count()))
                );
            }
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}