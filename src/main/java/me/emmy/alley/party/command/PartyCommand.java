package me.emmy.alley.party.command;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:32
 */
public class PartyCommand extends BaseCommand {
    @Override
    @Command(name = "party", aliases = "p")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&d&lParty Commands Help:"));
        player.sendMessage(CC.translate(" &f● &d/party create &7| Create a party"));
        player.sendMessage(CC.translate(" &f● &d/party disband &7| Disband a party"));
        player.sendMessage(CC.translate(" &f● &d/party leave &7| Leave a party"));
        player.sendMessage(CC.translate(" &f● &d/party info &7| Get information about your party"));
        player.sendMessage(CC.translate(" &f● &d/party chat &8(&7message&8) &7| Chat with your party"));
        player.sendMessage(CC.translate(" &f● &d/party accept &8(&7player&8) &7| Accept a party invite"));
        player.sendMessage(CC.translate(" &f● &d/party invite &8(&7player&8) &7| Invite a player to your party"));
        player.sendMessage(CC.translate(" &f● &d/party kick &8(&7player&8) &7| Kick a player out of your party"));
        player.sendMessage("");
    }
}
