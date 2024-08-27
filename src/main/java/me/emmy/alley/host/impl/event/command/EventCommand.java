package me.emmy.alley.host.impl.event.command;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:48
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

        player.sendMessage(" ");
        player.sendMessage(CC.translate("&b&lEvent Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/event host &7| Host an event"));
        player.sendMessage(CC.translate(" &f● &b/event join &7| Join an event"));
        player.sendMessage(CC.translate(" &f● &b/event leave &7| Leave an event"));

        if (player.hasPermission("alley.admin")) {
            player.sendMessage(" ");
            player.sendMessage(CC.translate("&c&lEvent Admin Commands:"));
            player.sendMessage(CC.translate(" &f● &c/event forcestart &7| Force start an event"));
            player.sendMessage(CC.translate(" &f● &c/event cancel &7| Cancel an event"));
        }
        player.sendMessage(" ");
    }
}
