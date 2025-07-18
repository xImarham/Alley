package dev.revere.alley.feature.item.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
public class CustomItemsCommand extends BaseCommand {
    @CommandData(
            name = "customitems",
            aliases = {"alleyitems", "specialitems"},
            usage = "/customitems",
            description = "List of commands for special items",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&6&lCustom Items Commands Help:",
                " &f● &6/customitems goldenhead &8(&7amount&8) &7| Gives you a golden head",
                ""
        ).forEach(line -> command.getPlayer().sendMessage(CC.translate(line)));
    }
}