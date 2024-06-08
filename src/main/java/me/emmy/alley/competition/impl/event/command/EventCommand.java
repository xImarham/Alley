package me.emmy.alley.competition.impl.event.command;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:48
 */
public class EventCommand extends BaseCommand {
    @Override
    @Command(name = "event", aliases = "events", permission = "alley.hostevent")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to host a competition."));
            return;
        }

        player.sendMessage(CC.translate("&9Event Help"));
        player.sendMessage(CC.translate(" &7- &9/event host"));
        player.sendMessage(CC.translate(" &7- &9/event join"));
        player.sendMessage(CC.translate(" &7- &9/event leave"));

        if (player.hasPermission("alley.admin")) {
            player.sendMessage(CC.translate(" &7- &c/event forcestart"));
            player.sendMessage(CC.translate(" &7- &c/event cancel"));
        }
    }
}
