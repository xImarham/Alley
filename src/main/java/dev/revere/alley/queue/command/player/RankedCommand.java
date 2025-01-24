package dev.revere.alley.queue.command.player;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.queue.menu.RankedMenu;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:31
 */

public class RankedCommand extends BaseCommand {
    @Override
    @Command(name = "ranked")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (!profile.getState().equals(EnumProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (profile.getParty() != null) {
            player.sendMessage(CC.translate("&cYou must leave your party to queue for ranked."));
            return;
        }

        new RankedMenu().openMenu(player);
    }
}
