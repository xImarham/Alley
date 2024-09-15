package me.emmy.alley.queue.command.player;

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
public class LeaveQueueCommand extends BaseCommand {
    @Command(name = "leavequeue")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (!profile.getState().equals(EnumProfileState.WAITING)) {
            player.sendMessage(CC.translate("&cYou are not in a queue."));
            return;
        }

        profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
    }
}
