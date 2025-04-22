package dev.revere.alley.feature.level.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import net.citizensnpcs.api.command.Command;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
public class LevelCommand extends BaseCommand {
    @CommandData(name = "level", aliases = {"levels"})
    @Override
    public void onCommand(CommandArgs command) {

    }
}
