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
 * Date: 02/06/2024 - 11:02
 */
public class NightCommand extends BaseCommand {
    @Override
    @Command(name = "night")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.setPlayerTime(18000L, false);
        player.sendMessage(CC.translate("&aYou have set the time to night."));
    }
}