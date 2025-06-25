package dev.revere.alley.profile.command.admin.ranked;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.command.admin.ranked.impl.RankedBanCommand;
import dev.revere.alley.profile.command.admin.ranked.impl.RankedUnbanCommand;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
public class RankedCommand extends BaseCommand {

    public RankedCommand() {
        new RankedBanCommand();
        new RankedUnbanCommand();
    }

    @CommandData(name = "ranked", isAdminOnly = true, description = "Manage ranked allowance.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                " ",
                "&6&lRanked Commands Help:",
                " &f● &6/ranked ban &8(&7player&8) &7| Ban a player from ranked matches.",
                " &f● &6/ranked unban &8(&7player&8) &7| Unban a player from ranked matches.",
                " "
        ).forEach(message -> player.sendMessage(CC.translate(message)));
    }
}
