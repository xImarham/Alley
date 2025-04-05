package dev.revere.alley.profile.settings.command.worldtime;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 10:57
 */
public class DayCommand extends BaseCommand {
    @Override
    @CommandData(name = "day")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        profile.getProfileData().getProfileSettingData().setTimeDay(player);
        player.sendMessage(CC.translate("&aYou have set the time to day."));
    }
}
