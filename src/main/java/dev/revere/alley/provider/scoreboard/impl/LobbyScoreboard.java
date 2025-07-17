package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.feature.level.ILevelService;
import dev.revere.alley.profile.IProfileService;
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
public class LobbyScoreboard implements IScoreboard {

    @Override
    public List<String> getLines(Profile profile) {
        IConfigService configService = Alley.getInstance().getService(IConfigService.class);
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        ILevelService levelService = Alley.getInstance().getService(ILevelService.class);

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = (profile.getParty() != null)
                ? configService.getScoreboardConfig().getStringList("scoreboard.lines.party")
                : configService.getScoreboardConfig().getStringList("scoreboard.lines.lobby");

        for (String line : template) {
            String processedLine = CC.translate(line)
                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                    .replace("{level}", String.valueOf(levelService.getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName()))
                    .replace("{playing}", String.valueOf(profileService.getProfiles().values().stream().filter(p -> p.getState() == EnumProfileState.PLAYING).count()))
                    .replace("{in-queue}", String.valueOf(profileService.getProfiles().values().stream().filter(p -> p.getState() == EnumProfileState.WAITING).count()));

            if (profile.getParty() != null) {
                processedLine = CC.translate(processedLine)
                        .replace("{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                        .replace("{party-privacy}", profile.getParty().isPrivate() ? "&cPrivate" : "&aPublic")
                        .replace("{party-leader}", profile.getParty().getLeader().getName());
            }

            scoreboardLines.add(processedLine);
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}