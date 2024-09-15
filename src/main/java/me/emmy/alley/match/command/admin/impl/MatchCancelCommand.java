package me.emmy.alley.match.command.admin.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchCancelCommand extends BaseCommand {
    @Command(name = "match.cancel", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /match cancel <player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING || profile.getMatch() == null) {
            player.sendMessage(CC.translate("&cThat player is not in a match."));
            return;
        }

        profile.getMatch().endMatch();
        player.sendMessage(CC.translate("&aYou have ended the match for &b" + target.getName() + "&a."));
    }
}
