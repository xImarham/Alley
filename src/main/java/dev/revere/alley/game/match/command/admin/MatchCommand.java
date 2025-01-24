package dev.revere.alley.game.match.command.admin;

import dev.revere.alley.game.match.command.admin.impl.MatchCancelCommand;
import dev.revere.alley.game.match.command.admin.impl.MatchStartCommand;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchCommand extends BaseCommand {

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
        sender.sendMessage(CC.translate(" &f● &b/match info &8(&7player&8) &7| Get match info of a player"));
        sender.sendMessage("");
    }
}
