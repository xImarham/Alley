package dev.revere.alley.feature.queue.command.player;

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
public class LeaveQueueCommand extends BaseCommand {
    @CommandData(name = "leavequeue")
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