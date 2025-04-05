package dev.revere.alley.profile.settings.command.toggle;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.locale.ProfileLocale;
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

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.getProfileData().getProfileSettingData().setShowScoreboardLines(!profile.getProfileData().getProfileSettingData().isShowScoreboardLines());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_SCOREBOARD_LINES.getMessage().replace("{status}", profile.getProfileData().getProfileSettingData().isShowScoreboardLines() ? "&aenabled" : "&cdisabled")));
    }
}
