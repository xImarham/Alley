package dev.revere.alley.feature.level.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.feature.level.command.impl.data.LevelAdminSetDisplayNameCommand;
import dev.revere.alley.feature.level.command.impl.data.LevelAdminSetMaxEloCommand;
import dev.revere.alley.feature.level.command.impl.data.LevelAdminSetMinEloCommand;
import dev.revere.alley.feature.level.command.impl.data.LevelAdminSetIconCommand;
import dev.revere.alley.feature.level.command.impl.manage.LevelAdminCreateCommand;
import dev.revere.alley.feature.level.command.impl.manage.LevelAdminDeleteCommand;
import dev.revere.alley.feature.level.command.impl.manage.LevelAdminListCommand;
import dev.revere.alley.feature.level.command.impl.manage.LevelAdminViewCommand;
import dev.revere.alley.util.chat.CC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminCommand extends BaseCommand {

    public LevelAdminCommand() {
        new LevelAdminCreateCommand();
        new LevelAdminDeleteCommand();
        new LevelAdminViewCommand();
        new LevelAdminListCommand();

        new LevelAdminSetDisplayNameCommand();
        new LevelAdminSetMaxEloCommand();
        new LevelAdminSetMinEloCommand();
        new LevelAdminSetIconCommand();
    }

    @CompleterData(name = "leveladmin")
    public List<String> kitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.plugin.getPluginConstant().getAdminPermissionPrefix())) {
            completion.addAll(Arrays.asList(
                    "create", "delete", "view", "setminelo",
                    "setmaxelo", "setdisplayname", "seticon", "list"
            ));
        }

        return completion;
    }

    @CommandData(name = "leveladmin", isAdminOnly = true, description = "Admin command for managing levels", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&b&lLevel Admin Commands Help:",
                " &f● &b/leveladmin create &8(&7levelName&8) &8(&7minElo&8) &8(&7maxElo&8) &7| Create a new level",
                " &f● &b/leveladmin delete &8(&7levelName&8) &7| Delete a level",
                " &f● &b/leveladmin list &7| List all levels",
                " &f● &b/leveladmin view &8(&7levelName&8) &7| View level info",
                " &f● &b/leveladmin setminelo &8(&7levelName&8) &8(&7minElo&8) &7| Set minimum Elo for a level",
                " &f● &b/leveladmin setmaxelo &8(&7levelName&8) &8(&7maxElo&8) &7| Set maximum Elo for a level",
                " &f● &b/leveladmin setdisplayname &8(&7levelName&8) &8(&7displayName&8) &7| Set display name for a level",
                " &f● &b/leveladmin seticon &8(&7levelName&8) &7| Set material for a level",
                ""
        ).forEach(line -> command.getSender().sendMessage(CC.translate(line)));
    }
}