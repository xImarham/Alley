package me.emmy.alley.game.match.command.player;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.settings.impl.KitSettingLivesImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class LeaveMatchCommand extends BaseCommand {
    @Command(name = "leave", aliases = {"suicide"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou are not in a match."));
            return;
        }

        if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
            profile.getMatch().getGamePlayer(player).getData().setLives(0);
            profile.getMatch().handleDeath(player);
            return;
        }

        profile.getMatch().handleDeath(player);
        //profile.getMatch().sendMessage("&c" + player.getName() + " has committed suicide.");
        player.sendMessage(CC.translate("&cYou've commited suicide :("));
    }
}
