package me.emmy.alley.match.command.admin;

import me.emmy.alley.match.command.admin.impl.MatchCancelCommand;
import me.emmy.alley.match.command.admin.impl.MatchStartCommand;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchCommand extends BaseCommand {

    /**
     * Register all Match subcommands in the constructor
     */
    public MatchCommand() {
        new MatchStartCommand();
        new MatchCancelCommand();
    }

    @Override
    @Command(name = "match", permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&b&lMatch Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &b/match start &8(&7p1&8) &8(&7p2&8) &8(&7arena&8) &8(&7kit&8) &7| Start a match"));
        sender.sendMessage(CC.translate(" &f● &b/match cancel &8(&7player&8) &7| Cancel a match"));
        sender.sendMessage("");
    }
}
