package dev.revere.alley.game.match.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchCancelCommand extends BaseCommand {
    @CommandData(name = "match.cancel", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/match cancel &b<player>"));
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
