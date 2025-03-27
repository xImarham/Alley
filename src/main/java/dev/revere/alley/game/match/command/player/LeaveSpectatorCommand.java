package dev.revere.alley.game.match.command.player;

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
 * @date 5/21/2024
 */
public class LeaveSpectatorCommand extends BaseCommand {
    @CommandData(name = "leavespectator", aliases = {"unspec"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.SPECTATING) {
            player.sendMessage(CC.translate("&cYou are not spectating a match."));
            return;
        }

        profile.getMatch().removeSpectator(player, true);
    }
}
