package dev.revere.alley.base.spawn.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.hotbar.IHotbarService;
import dev.revere.alley.base.spawn.ISpawnService;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.ffa.enums.EnumFFAState;
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
        IProfileService profileService = this.plugin.getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        EnumProfileState state = profile.getState();

        switch (state) {
            case FFA:
                AbstractFFAMatch ffaMatch = profile.getFfaMatch();
                if (ffaMatch == null) return;

                if (ffaMatch.getGameFFAPlayer(player).getState() == EnumFFAState.FIGHTING) {
                    ffaMatch.teleportToSafeZone(player);
                } else {
                    ffaMatch.leave(player);
                }
                break;
            case PLAYING:
                player.sendMessage(CC.translate("&cYou cannot do this right now."));
                break;
            case SPECTATING:
                profile.getMatch().removeSpectator(player, false);
                break;
            default:
                this.sendToSpawn(player);
                break;
        }
    }

    /**
     * Sends the player to the spawn location and resets their state.
     *
     * @param player The player to send to spawn.
     */
    private void sendToSpawn(Player player) {
        PlayerUtil.reset(player, false);
        this.plugin.getService(ISpawnService.class).teleportToSpawn(player);
        this.plugin.getService(IHotbarService.class).applyHotbarItems(player);

        player.sendMessage(CC.translate(this.plugin.getService(IConfigService.class).getMessagesConfig().getString("spawn.teleported")));
    }
}