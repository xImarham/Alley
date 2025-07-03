package dev.revere.alley.profile.command.player.setting.worldtime;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 10:59
 */
public class ResetTimeCommand extends BaseCommand {
    @Override
    @CommandData(name = "resettime", aliases = "currenttime")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        profile.getProfileData().getSettingData().setTimeDefault(player);
        player.sendMessage(CC.translate("&aYou have reset your world time."));
    }
}
