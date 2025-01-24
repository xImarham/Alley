package dev.revere.alley.game.ffa.command.admin;

import dev.revere.alley.game.ffa.command.player.FFAJoinCommand;
import dev.revere.alley.game.ffa.command.player.FFALeaveCommand;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 00:14
 */
public class FFACommand extends BaseCommand {

    /**
     * Register all FFA subcommands in the constructor
     */
    public FFACommand() {
        new FFACreateCommand();
        new FFADeleteCommand();
        new FFAKickCommand();
        new FFAListCommand();
        new FFAListPlayersCommand();
        new FFAMaxPlayersCommand();
        new FFAJoinCommand();
        new FFALeaveCommand();
    }

    @Override
    @Command(name = "ffa", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&bFFA Help"));
        player.sendMessage(CC.translate("&7- &b/ffa &7- &fShow this help message."));
        player.sendMessage(CC.translate("&7- &b/ffa create &7- &fCreate a new FFA arena."));
        player.sendMessage(CC.translate("&7- &b/ffa delete &7- &fDelete an existing FFA arena."));
        player.sendMessage(CC.translate("&7- &b/ffa kick &7- &fKick a player out of FFA."));
        player.sendMessage(CC.translate("&7- &b/ffa list &7- &fList current ffa matches."));
        player.sendMessage(CC.translate("&7- &b/ffa listplayers &7- &fList all players playing on FFA."));
        player.sendMessage(CC.translate("&7- &b/ffa maxplayers &7- &fSet the max player count."));


    }
}
