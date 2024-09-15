package me.emmy.alley.profile.command;

import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:03
 */
public class ChallengesCommand extends BaseCommand {
    @Command(name = "challenges")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_STILL_IN_DEVELOPMENT));
    }
}
