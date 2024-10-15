package me.emmy.alley.command.admin.debug;

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
 * @date 30/05/2024 - 12:15
 */
public class StateCommand extends BaseCommand {
    @Override
    @Command(name = "state", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        player.sendMessage(CC.translate("&cYour current state is &4" + profile.getState().getName()));
        player.sendMessage(CC.translate(profile.getMatch() != null ? "&cYou are in a match:" + profile.getMatch().getState().getName() : "&cYou are not in a match."));
    }
}
