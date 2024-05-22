package me.emmy.alley.commands.global.party;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 22/05/2024 - 20:32
 */

public class PartyCommand extends BaseCommand {
    @Override
    @Command(name = "party")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("&d&lParty Commands Help:"));
        player.sendMessage(CC.translate(" &f● &d/party create &7| Create a party"));
        player.sendMessage(CC.translate(" &f● &d/party disband &7| Disband a party"));
        player.sendMessage(CC.translate(" &f● &d/party leave &7| Leave a party"));
        player.sendMessage(CC.translate(" &f● &d/party invite &7(&8player&7) &7| Invite a player to your party"));
        player.sendMessage(CC.translate(" &f● &d/party kick &7(&8player&7) &7| Kick a player out of your party"));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");
    }
}
