package dev.revere.alley.profile.settings.command.toggle;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 23:35
 */

public class ToggleScoreboardCommand extends BaseCommand {
    @Override
    @Command(name = "togglescoreboard")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.getProfileData().getProfileSettingData().setScoreboardEnabled(!profile.getProfileData().getProfileSettingData().isScoreboardEnabled());

        player.sendMessage(CC.translate(Locale.TOGGLED_SCOREBOARD.getMessage().replace("{status}", profile.getProfileData().getProfileSettingData().isScoreboardEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
