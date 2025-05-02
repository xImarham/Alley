package dev.revere.alley.profile.command.player.setting.toggle;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.locale.ProfileLocale;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 27/04/2025
 */
public class ToggleProfanityFilterCommand extends BaseCommand {
    @CommandData(name = "toggleprofanityfilter", aliases = {"tpf"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.getProfileData().getProfileSettingData().setProfanityFilterEnabled(!profile.getProfileData().getProfileSettingData().isProfanityFilterEnabled());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_PROFANITY_FILTER.getMessage().replace("{status}", profile.getProfileData().getProfileSettingData().isProfanityFilterEnabled() ? "&aenabled" : "&cdisabled")));
    }
}