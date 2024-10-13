package me.emmy.alley.profile.settings.command.worldtime;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 10:59
 */
public class ResetTimeCommand extends BaseCommand {
    @Override
    @Command(name = "resettime", aliases = "currenttime")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        profile.getProfileData().getProfileSettingData().setTimeDefault(player);
        player.sendMessage(CC.translate("&aYou have reset your world time."));
    }
}
