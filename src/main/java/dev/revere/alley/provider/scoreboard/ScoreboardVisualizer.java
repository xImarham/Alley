package dev.revere.alley.provider.scoreboard;

import dev.revere.alley.api.assemble.interfaces.IAssembleAdapter;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.provider.scoreboard.impl.FFAScoreboard;
import dev.revere.alley.provider.scoreboard.impl.LobbyScoreboard;
import dev.revere.alley.provider.scoreboard.impl.QueueScoreboard;
import dev.revere.alley.provider.scoreboard.impl.SpectatorScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.MatchScoreboard;
import dev.revere.alley.tool.animation.IAnimationRepository;
import dev.revere.alley.tool.animation.enums.EnumAnimationType;
import dev.revere.alley.tool.animation.type.config.impl.ScoreboardTitleAnimationImpl;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 27/03/2024 - 14:27
 */
public class ScoreboardVisualizer implements IAssembleAdapter {
    private final IAnimationRepository animationRepository;
    private final IProfileService profileService;
    private final IConfigService configService;

    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard();
    private final QueueScoreboard queueScoreboard = new QueueScoreboard();
    private final MatchScoreboard matchScoreboard = new MatchScoreboard();
    private final SpectatorScoreboard spectatorScoreboard = new SpectatorScoreboard();
    private final FFAScoreboard ffaScoreboard = new FFAScoreboard();

    public ScoreboardVisualizer(IAnimationRepository animationRepository, IProfileService profileService, IConfigService configService) {
        this.animationRepository = animationRepository;
        this.profileService = profileService;
        this.configService = configService;
    }

    @Override
    public String getTitle(Player player) {
        return this.animationRepository.getAnimation(ScoreboardTitleAnimationImpl.class, EnumAnimationType.CONFIG).getText();
    }

    /**
     * Get the lines of the scoreboard.
     *
     * @param player The player to get the lines for.
     * @return The lines of the scoreboard.
     */
    @Override
    public List<String> getLines(Player player) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());

        if (profile.getProfileData().getSettingData().isScoreboardEnabled()) {

            if (profile.getState() == EnumProfileState.EDITING) {
                return Collections.emptyList();
            }

            List<String> lines = new ArrayList<>();

            switch (profile.getState()) {
                case LOBBY:
                    lines.addAll(this.lobbyScoreboard.getLines(profile));
                    break;
                case WAITING:
                    lines.addAll(this.queueScoreboard.getLines(profile));
                    break;
                case PLAYING:
                    lines.addAll(this.matchScoreboard.getLines(profile, player));
                    break;
                case SPECTATING:
                    lines.addAll(this.spectatorScoreboard.getLines(profile));
                    break;
                case FFA:
                    lines.addAll(this.ffaScoreboard.getLines(profile, player));
                    break;
            }

            List<String> footer = this.configService.getScoreboardConfig().getStringList("scoreboard.footer-addition");
            footer.forEach(line -> lines.add(CC.translate(line)));

            lines.replaceAll(line -> line.replace("{sidebar}", this.getScoreboardLines(profile)));
            return lines;
        }
        return null;
    }

    /**
     * Method to either show the scoreboard lines or not.
     *
     * @param profile The profile to get the scoreboard lines for.
     * @return The scoreboard lines.
     */
    private String getScoreboardLines(Profile profile) {
        if (profile.getProfileData().getSettingData().isShowScoreboardLines()) {
            return this.configService.getScoreboardConfig().getString("scoreboard.sidebar-format");
        }
        return "";
    }
}