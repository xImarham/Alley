package dev.revere.alley.game.match.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.match.menu.SpectatorTeleportMenu;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public class ViewPlayersCommand extends BaseCommand {
    @CommandData(name = "viewplayers")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        IProfileService profileService = this.plugin.getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.SPECTATING) {
            player.sendMessage(CC.translate("&cYou cannot do this in your current state."));
            return;
        }

        new SpectatorTeleportMenu(profile.getMatch()).openMenu(player);
    }
}
