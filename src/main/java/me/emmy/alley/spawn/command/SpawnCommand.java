package me.emmy.alley.spawn.command;

import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.PlayerUtil;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
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

        PlayerUtil.reset(player);
        Alley.getInstance().getSpawnHandler().teleportToSpawn(player);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        player.sendMessage(CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("spawn.teleported")));
    }
}
