package me.emmy.alley.profile.settings.command.worldtime;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 13/10/2024 - 10:25
 */
public class ToggleWorldTimeCommand extends BaseCommand {
    @Command(name = "toggleworldtime")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

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
