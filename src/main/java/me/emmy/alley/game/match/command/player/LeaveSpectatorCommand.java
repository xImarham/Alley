package me.emmy.alley.game.match.command.player;

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
 * @date 5/21/2024
 */
public class LeaveSpectatorCommand extends BaseCommand {
    @Command(name = "leavespectator", aliases = {"unspec"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.SPECTATING) {
            player.sendMessage(CC.translate("&cYou are not spectating a match."));
            return;
        }

        profile.getMatch().removeSpectator(player, true);
    }
}
