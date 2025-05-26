package dev.revere.alley.base.queue.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class LeaveQueueCommand extends BaseCommand {
    @CommandData(name = "leavequeue")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (!profile.getState().equals(EnumProfileState.WAITING)) {
            player.sendMessage(CC.translate("&cYou are not in a queue."));
            return;
        }

//        ParkourService parkourService = Alley.getInstance().getParkourService();
//        if (parkourService.isPlaying(player)) {
//            parkourService.removePlayer(player, false);
//        }

        profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
    }
}