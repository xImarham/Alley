package dev.revere.alley.command.impl.donator;

import dev.revere.alley.Alley;
import dev.revere.alley.game.host.HostMenu;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:31
 */
public class HostCommand extends BaseCommand {
    @Override
    @Command(name = "host", permission = "alley.host")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to host a competition."));
            return;
        }

        new HostMenu().openMenu(player);
    }
}
