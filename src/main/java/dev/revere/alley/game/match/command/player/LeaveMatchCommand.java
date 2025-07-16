package dev.revere.alley.game.match.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class LeaveMatchCommand extends BaseCommand {
    @CommandData(name = "leave", aliases = {"suicide"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        IProfileService profileService = this.plugin.getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou are not in a match."));
            return;
        }

        profile.getMatch().handleDisconnect(player);
        player.sendMessage(CC.translate("&cYou've commited suicide :("));
    }
}