package me.emmy.alley.party.command;

import me.emmy.alley.party.command.impl.leader.PartyCreateCommand;
import me.emmy.alley.party.command.impl.leader.PartyDisbandCommand;
import me.emmy.alley.party.command.impl.leader.PartyInviteCommand;
import me.emmy.alley.party.command.impl.leader.PartyKickCommand;
import me.emmy.alley.party.command.impl.member.PartyAcceptCommand;
import me.emmy.alley.party.command.impl.member.PartyChatCommand;
import me.emmy.alley.party.command.impl.member.PartyInfoCommand;
import me.emmy.alley.party.command.impl.member.PartyLeaveCommand;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:32
 */
public class PartyCommand extends BaseCommand {

    /**
     * Register all Party subcommands in the constructor
     */
    public PartyCommand() {
        new PartyCreateCommand();
        new PartyLeaveCommand();
        new PartyInfoCommand();
        new PartyChatCommand();
        new PartyInviteCommand();
        new PartyAcceptCommand();
        new PartyDisbandCommand();
        new PartyKickCommand();
    }

    @Override
    @Command(name = "party", aliases = "p")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lParty Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/party create &7| Create a party"));
        player.sendMessage(CC.translate(" &f● &b/party disband &7| Disband a party"));
        player.sendMessage(CC.translate(" &f● &b/party leave &7| Leave a party"));
        player.sendMessage(CC.translate(" &f● &b/party info &7| Get information about your party"));
        player.sendMessage(CC.translate(" &f● &b/party chat &8(&7message&8) &7| Chat with your party"));
        player.sendMessage(CC.translate(" &f● &b/party accept &8(&7player&8) &7| Accept a party invite"));
        player.sendMessage(CC.translate(" &f● &b/party invite &8(&7player&8) &7| Invite a player to your party"));
        player.sendMessage(CC.translate(" &f● &b/party kick &8(&7player&8) &7| Kick a player out of your party"));
        player.sendMessage("");
    }
}
