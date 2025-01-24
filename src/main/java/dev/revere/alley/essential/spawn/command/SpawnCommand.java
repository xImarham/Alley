package dev.revere.alley.essential.spawn.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.hotbar.enums.HotbarType;
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
    @Command(name = "spawn", permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA || profile.getState() == EnumProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou cannot teleport to spawn while in this state."));
            return;
        }

        PlayerUtil.reset(player, false);
        Alley.getInstance().getSpawnService().teleportToSpawn(player);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        player.sendMessage(CC.translate(Alley.getInstance().getConfigService().getMessagesConfig().getString("spawn.teleported")));
    }
}
