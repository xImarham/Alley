package me.emmy.alley.profile.settings.playersettings.command.worldtime;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 02/06/2024 - 10:57
 */
public class DayCommand extends BaseCommand {
    @Override
    @Command(name = "day")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.setPlayerTime(6000L, false);
        player.sendMessage(CC.translate("&aYou have set the time to day."));
    }
}
