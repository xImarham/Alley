package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.adapter.core.ICoreAdapter;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.feature.level.ILevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.feature.music.IMusicService;
import dev.revere.alley.feature.music.MusicSession;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.provider.scoreboard.IScoreboard;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        IMusicService musicService = Alley.getInstance().getService(IMusicService.class);
        ICoreAdapter coreAdapter = Alley.getInstance().getService(ICoreAdapter.class);

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = (profile.getParty() != null)
                ? configService.getScoreboardConfig().getStringList("scoreboard.lines.party")
                : configService.getScoreboardConfig().getStringList("scoreboard.lines.lobby");

        Optional<MusicSession> musicStateOptional = musicService.getMusicState(profile.getUuid());

        int currentElo = profile.getProfileData().getElo();
        LevelData currentLevel = levelService.getLevel(currentElo);

        for (String line : template) {
            if (line.equalsIgnoreCase("{music}")) {
                musicStateOptional.ifPresent(state -> {
                    List<String> musicTemplate = configService.getScoreboardConfig().getStringList("scoreboard.lines.music");

                    int elapsedSeconds = state.getElapsedSeconds();
                    int totalSeconds = state.getDisc().getDuration();

                    String elapsedTime = TimeUtil.formatTimeFromSeconds(elapsedSeconds);
                    String totalTime = TimeUtil.formatTimeFromSeconds(totalSeconds);
                    String duration = elapsedTime + " / " + totalTime;

                    for (String musicLine : musicTemplate) {
                        scoreboardLines.add(CC.translate(musicLine)
                                .replace("{song-name}", state.getDisc().getTitle())
                                .replace("{song-duration}", duration)
                        );
                    }
                });
                continue;
            }

            String processedLine = CC.translate(line)
                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                    .replace("{level}", currentLevel.getDisplayName())
                    .replace("{level_progress_bar}", levelService.getProgressBar(currentElo))
                    .replace("{level_progress_details}", levelService.getProgressDetails(currentElo))
                    .replace("{rank}", coreAdapter.getCore().getRankColor(Bukkit.getPlayer(profile.getUuid())) + coreAdapter.getCore().getRankName(Bukkit.getPlayer(profile.getUuid())))
                    .replace("{playing}", String.valueOf(profileService.getProfiles().values().stream().filter(p -> p.getState() == EnumProfileState.PLAYING).count()))
                    .replace("{in-queue}", String.valueOf(profileService.getProfiles().values().stream().filter(p -> p.getState() == EnumProfileState.WAITING).count()));

            if (profile.getParty() != null) {
                processedLine = CC.translate(processedLine)
                        .replace("{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                        .replace("{party-privacy}", profile.getParty().isPrivate() ? "&cPrivate" : "&aPublic")
                        .replace("{party-leader}", profileService.getProfile(profile.getParty().getLeader().getUniqueId()).getFancyName());
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