package me.emmy.alley.profile.settings.playersettings.command.worldtime;

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
public class CurrentTimeCommand extends BaseCommand {
    @Override
    @Command(name = "currenttime")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.resetPlayerTime();
        player.sendMessage(CC.translate("&aYou have reset your world time."));
    }
}
