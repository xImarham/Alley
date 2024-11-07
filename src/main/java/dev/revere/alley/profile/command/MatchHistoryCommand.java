package dev.revere.alley.profile.command;

import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:05
 */
public class MatchHistoryCommand extends BaseCommand {
    @Command(name = "matchhistory")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_STILL_IN_DEVELOPMENT));
    }
}
