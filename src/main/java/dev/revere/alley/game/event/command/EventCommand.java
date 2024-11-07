package dev.revere.alley.game.event.command;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
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