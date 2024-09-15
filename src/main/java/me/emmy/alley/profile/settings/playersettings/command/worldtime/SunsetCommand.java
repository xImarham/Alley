package me.emmy.alley.profile.settings.playersettings.command.worldtime;

import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 11:05
 */
public class SunsetCommand extends BaseCommand {
    @Override
    @Command(name = "sunset")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.setPlayerTime(12000, false);
        player.sendMessage(CC.translate("&aYou have set the time to sunset."));
    }
}