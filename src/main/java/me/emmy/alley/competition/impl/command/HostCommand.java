package me.emmy.alley.competition.impl.command;

import me.emmy.alley.Alley;
import me.emmy.alley.competition.menu.CompetitionsMenu;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:31
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

        new CompetitionsMenu().openMenu(player);
    }
}
