package dev.revere.alley.game.ffa.command.impl.player;

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
 * @date 5/27/2024
 */
public class FFASpawnCommand extends BaseCommand {
    @CommandData(name = "ffa.spawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        IProfileService profileService = this.plugin.getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.FFA) {
            player.sendMessage(CC.translate("&cYou can only use this command in an ffa matcj."));
            return;
        }

        profile.getFfaMatch().teleportToSafeZone(player);
    }
}