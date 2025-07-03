package dev.revere.alley.base.spawn.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.hotbar.IHotbarService;
import dev.revere.alley.base.spawn.ISpawnService;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 19:01
 */
public class SpawnCommand extends BaseCommand {
    @Override
    @CommandData(name = "spawn", isAdminOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA || profile.getState() == EnumProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou cannot teleport to spawn while in this state."));
            return;
        }

        PlayerUtil.reset(player, false);
        Alley.getInstance().getService(ISpawnService.class).teleportToSpawn(player);
        Alley.getInstance().getService(IHotbarService.class).applyHotbarItems(player);
        player.sendMessage(CC.translate(Alley.getInstance().getService(IConfigService.class).getMessagesConfig().getString("spawn.teleported")));
    }
}