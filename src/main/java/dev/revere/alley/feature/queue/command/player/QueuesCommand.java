package dev.revere.alley.feature.queue.command.player;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.feature.queue.menu.QueuesMenu;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 16:45
 */
public class QueuesCommand extends BaseCommand {
    @Override
    @CommandData(name = "queues", aliases = {"selectqueue", "joinqueue"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (!profile.getState().equals(EnumProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (profile.getParty() != null) {
            player.sendMessage(CC.translate("&cYou must leave your party to queue for a game."));
            return;
        }

        new QueuesMenu().openMenu(player);
    }
}
