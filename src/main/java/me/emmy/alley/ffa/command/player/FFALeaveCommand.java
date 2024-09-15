package me.emmy.alley.ffa.command.player;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFALeaveCommand extends BaseCommand {
    @Command(name = "ffa.leave", aliases = "leaveffa")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getFfaMatch() == null) {
            player.sendMessage(CC.translate("&cYou are not in a FFA match."));
            return;
        }

        profile.getFfaMatch().leave(player);
    }
}
