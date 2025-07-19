package dev.revere.alley.profile.command.player.setting.toggle;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.ProfileLocale;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class ToggleLobbyMusicCommand extends BaseCommand {
    @CommandData(name = "togglelobbymusic")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        IProfileService profileService = this.plugin.getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setLobbyMusicEnabled(!profile.getProfileData().getSettingData().isLobbyMusicEnabled());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_LOBBY_MUSIC.getMessage().replace("{status}", profile.getProfileData().getSettingData().isLobbyMusicEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
