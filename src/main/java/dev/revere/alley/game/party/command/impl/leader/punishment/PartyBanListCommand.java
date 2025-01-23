package dev.revere.alley.game.party.command.impl.leader.punishment;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 11/12/2024 - 13:46
 */
public class PartyBanListCommand extends BaseCommand {
    @Command(name = "party.banlist", aliases = "p.banlist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&c&lBanned Members in your party:"));
        Alley.getInstance().getPartyHandler().getPartyByLeader(player).getBannedMembers().forEach(bannedMember -> player.sendMessage(CC.translate("&7- &c" + Bukkit.getPlayer(bannedMember).getName())));
        player.sendMessage("");
    }
}