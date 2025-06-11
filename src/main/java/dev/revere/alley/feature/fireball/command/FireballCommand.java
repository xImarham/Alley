package dev.revere.alley.feature.fireball.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.fireball.command.impl.FireballHorizontalCommand;
import dev.revere.alley.feature.fireball.command.impl.FireballRangeCommand;
import dev.revere.alley.feature.fireball.command.impl.FireballVerticalCommand;
import dev.revere.alley.util.chat.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
public class FireballCommand extends BaseCommand {

    public FireballCommand() {
        new FireballHorizontalCommand();
        new FireballRangeCommand();
        new FireballVerticalCommand();
    }

    @CommandData(name = "fireball", aliases = {"fb"}, isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&b&lFireball Commands Help:",
                " &f● &b/fireball horizontal &8(&7value&8) &7| Set horizontal knockback.",
                " &f● &b/fireball vertical &8(&7value&8) &7| Set vertical knockback.",
                " &f● &b/fireball range &8(&7value&8) &7| Set explosion range that affects players.",
                ""
        ).forEach(line -> command.getSender().sendMessage(CC.translate(line)));
    }
}