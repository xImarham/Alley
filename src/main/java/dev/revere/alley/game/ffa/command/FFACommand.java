package dev.revere.alley.game.ffa.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.ffa.command.impl.*;
import dev.revere.alley.game.ffa.command.impl.player.FFAJoinCommand;
import dev.revere.alley.game.ffa.command.impl.player.FFALeaveCommand;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 00:14
 */
public class FFACommand extends BaseCommand {

    /**
     * Register all FFA subcommands in the constructor
     */
    public FFACommand() {
        new FFAKickCommand();
        new FFAListCommand();
        new FFAListPlayersCommand();
        new FFAMaxPlayersCommand();
        new FFAJoinCommand();
        new FFALeaveCommand();
        new FFASetSafeZoneCommand();
        new FFASetSpawnCommand();
    }

    @Override
    @CommandData(name = "ffa", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&b&lFFA Commands",
                " &f● &b/ffa kick &7| Kick a player out of FFA",
                " &f● &b/ffa list &7| List current FFA matches",
                " &f● &b/ffa listplayers &7| List all players playing on FFA",
                " &f● &b/ffa maxplayers &7| Set the max player count",
                " &f● &b/ffa setspawn &7| Set the spawn location for FFA",
                " &f● &b/ffa setsafezone &7| Set the safezone for FFA",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}