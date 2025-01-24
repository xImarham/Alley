package dev.revere.alley.command.impl.admin.management;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 24/05/2024 - 18:45
 * @credit dori
 */
public class PlaytimeCommand extends BaseCommand {
    @Override
    @Command(name = "playtime", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /playtime (player)"));
            return;
        }

        String target = args[0];
        Player targetPlayer = Bukkit.getPlayer(target);

        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cThe player you are trying to check is not online."));
            return;
        }

        if (!targetPlayer.hasPlayedBefore()) {
            player.sendMessage(CC.translate("&cThe player you are trying to check has never played before."));
            return;
        }

        double ticksPlayed = targetPlayer.getStatistic(Statistic.PLAY_ONE_TICK);

        double secondsPlayed = ticksPlayed / 20.0;
        double minutesPlayed = secondsPlayed / 60.0;
        double hoursPlayed = minutesPlayed / 60.0;
        double daysPlayed = hoursPlayed / 24.0;

        int days = (int)daysPlayed;
        int hours = (int)((daysPlayed - (double)days) * 24.0);
        int minutes = (int)((hoursPlayed - (double)(hours + days * 24)) * 60.0);
        int seconds = (int)((minutesPlayed - (double)(minutes + hours * 60 + days * 60 * 24)) * 60.0);

        player.sendMessage(CC.translate(Alley.getInstance().getConfigService().getMessagesConfig().getString("playtime.message")
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(seconds))
                .replace("{target}", targetPlayer.getName())))
        ;

    }
}
