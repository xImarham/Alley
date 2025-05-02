package dev.revere.alley.feature.spawn.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
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
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA || profile.getState() == EnumProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou cannot teleport to spawn while in this state."));
            return;
        }

        PlayerUtil.reset(player, false);
        this.plugin.getSpawnService().teleportToSpawn(player);
        this.plugin.getHotbarService().applyHotbarItems(player);
        player.sendMessage(CC.translate(this.plugin.getConfigService().getMessagesConfig().getString("spawn.teleported")));
    }
}