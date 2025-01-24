package dev.revere.alley.profile.division.command;

import dev.revere.alley.profile.division.command.impl.DivisionListCommand;
import dev.revere.alley.profile.division.command.impl.DivisionMenuCommand;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionCommand extends BaseCommand {

    /**
     * Register all Division subcommands in the constructor
     */
    public DivisionCommand() {
        new DivisionMenuCommand();
        new DivisionListCommand();
    }

    @Command(name = "division", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(" ");
        player.sendMessage(CC.translate("&b&lDivision Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/division list &7| View all divisions"));
        player.sendMessage("");
    }
}
