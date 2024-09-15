package me.emmy.alley.commands.admin.debug;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 13:44
 */
public class FFAStateCommand extends BaseCommand {
    @Override
    @Command(name = "ffastate", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getFfaMatch() == null) {
            player.sendMessage(CC.translate("&cYou are not in a FFA match."));
            return;
        }

        player.sendMessage(CC.translate("&cYour current FFA state is &4" + profile.getFfaMatch().getState().getName()));
    }
}
