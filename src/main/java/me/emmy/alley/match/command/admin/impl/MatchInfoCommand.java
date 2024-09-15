package me.emmy.alley.match.command.admin.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 11:39
 */
public class MatchInfoCommand extends BaseCommand {
    @Command(name = "match.info", permission = "alley.command.match.info", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&cUsage: /match info <player>"));
            return;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        if (profile.getMatch() == null) {
            sender.sendMessage(CC.translate("&cThis player is not in a match."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&c&lMatch Information"));
        
        sender.sendMessage(CC.translate(" &f&l● &fPlayers:"));
        profile.getMatch().getParticipants().forEach(participant -> 
                sender.sendMessage(CC.translate("&c- " + participant.getPlayer().getPlayer().getName()))
        );
        
        sender.sendMessage(CC.translate(" &f&l● &fSpectators:"));
        if (profile.getMatch().getMatchSpectators().isEmpty()) {
            sender.sendMessage(CC.translate("&c- None"));
        } else {
            profile.getMatch().getMatchSpectators().forEach(spectator -> 
                    sender.sendMessage(CC.translate("&c- " + Bukkit.getPlayer(spectator).getName()))
            );
        }
        sender.sendMessage(CC.translate(" &f&l● &fKit: &c" + profile.getMatch().getMatchKit().getName()));
        sender.sendMessage(CC.translate(" &f&l● &fArena: &c" + profile.getMatch().getMatchArena().getName()));
        sender.sendMessage(CC.translate(" &f&l● &fState: &c" + profile.getMatch().getMatchState()));
        sender.sendMessage("");
    }
}
