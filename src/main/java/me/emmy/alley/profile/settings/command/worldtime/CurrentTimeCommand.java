package me.emmy.alley.profile.settings.command.worldtime;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 02/06/2024 - 10:59
 */
public class CurrentTimeCommand extends BaseCommand {
    @Override
    @Command(name = "currenttime")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.resetPlayerTime();
        player.sendMessage(CC.translate("&aYou have reset your world time."));
    }
}
