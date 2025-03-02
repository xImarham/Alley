package dev.revere.alley.command.impl.main;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @Command(name = "alley", aliases = {"emmy", "remi", "revere"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Arrays.asList(
                "",
                "     &b&lAlley",
                "      &f┃ Authors: &b" + Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "      &f┃ Version: &b" + Alley.getInstance().getDescription().getVersion(),
                "",
                "     &b&lDescription:",
                "      &f┃ " + Alley.getInstance().getDescription().getDescription(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (sender.hasPermission("alley.admin")) {
            Arrays.asList(
                    "     &b&lAdmin Help",
                    "      &f┃ /alley reload &7- &bReloads the plugin.",
                    "      &f┃ /alley debug &7- &bDisplays info for development purposes.",
                    ""
            ).forEach(line -> sender.sendMessage(CC.translate(line)));
        }
    }
}