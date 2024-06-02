package me.emmy.alley.profile.settings.command.worldtime;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 02/06/2024 - 11:05
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