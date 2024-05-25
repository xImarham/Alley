package me.emmy.alley.leaderboard.command;

import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/05/2024 - 11:29
 */

public class LeaderboardCommand extends BaseCommand {
    @Override
    @Command(name = "leaderboard", aliases = {"leaderboards", "lb"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_CMD));
    }
}
