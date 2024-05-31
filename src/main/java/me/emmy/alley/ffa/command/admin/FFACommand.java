package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: alley
 * Date: 01/06/2024 - 00:14
 */
public class FFACommand extends BaseCommand {
    @Override
    @Command(name = "ffa", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&dFFA Help"));
        player.sendMessage(CC.translate("&7- &d/ffa &7- &fShow this help message."));
        player.sendMessage(CC.translate("&7- &d/ffa create &7- &fCreate a new FFA arena."));
        player.sendMessage(CC.translate("&7- &d/ffa delete &7- &fDelete an existing FFA arena."));
        player.sendMessage(CC.translate("&7- &d/ffa kick &7- &fKick a player out of FFA."));
        player.sendMessage(CC.translate("&7- &d/ffa list &7- &fList current ffa matches."));
        player.sendMessage(CC.translate("&7- &d/ffa listplayers &7- &fList all players playing on FFA."));
        player.sendMessage(CC.translate("&7- &d/ffa maxplayers &7- &fSet the max player count."));


    }
}
