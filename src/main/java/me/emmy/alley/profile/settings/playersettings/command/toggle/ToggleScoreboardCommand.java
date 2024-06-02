package me.emmy.alley.profile.settings.playersettings.command.toggle;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 23:35
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
