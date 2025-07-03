package dev.revere.alley.profile.command.player.setting.toggle;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.ProfileLocale;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
public class ToggleScoreboardLinesCommand extends BaseCommand {
    @CommandData(name = "togglescoreboardlines", aliases = "tsl", description = "Toggle the scoreboard lines.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setShowScoreboardLines(!profile.getProfileData().getSettingData().isShowScoreboardLines());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_SCOREBOARD_LINES.getMessage().replace("{status}", profile.getProfileData().getSettingData().isShowScoreboardLines() ? "&aenabled" : "&cdisabled")));
    }
}
