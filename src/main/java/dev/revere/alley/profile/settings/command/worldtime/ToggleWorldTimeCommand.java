package dev.revere.alley.profile.settings.command.worldtime;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 13/10/2024 - 10:25
 */
public class ToggleWorldTimeCommand extends BaseCommand {
    @CommandData(name = "toggleworldtime")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        switch (profile.getProfileData().getProfileSettingData().getWorldTime()) {
            case DEFAULT:
                profile.getProfileData().getProfileSettingData().setTimeDay(player);
                player.sendMessage(CC.translate("&aYou have set the time to day."));
                break;
            case DAY:
                profile.getProfileData().getProfileSettingData().setTimeSunset(player);
                player.sendMessage(CC.translate("&aYou have set the time to sunset."));
                break;
            case SUNSET:
                profile.getProfileData().getProfileSettingData().setTimeNight(player);
                player.sendMessage(CC.translate("&aYou have set the time to night."));
                break;
            case NIGHT:
                profile.getProfileData().getProfileSettingData().setTimeDefault(player);
                player.sendMessage(CC.translate("&aYou have reset your world time."));
                break;
        }
    }
}
