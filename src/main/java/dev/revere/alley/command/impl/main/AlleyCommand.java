package dev.revere.alley.command.impl.main;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @CommandData(name = "alley", aliases = {"apractice", "aprac", "practice", "emmy", "remi", "revere"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Arrays.asList(
                "",
                "     &b&lAlley Practice",
                "      &f┃ Authors: &b" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "      &f┃ Version: &b" + this.plugin.getDescription().getVersion(),
                "",
                "     &b&lDescription:",
                "      &f┃ " + this.plugin.getDescription().getDescription(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (sender.hasPermission(this.plugin.getPluginConstant().getAdminPermissionPrefix())) {
            Arrays.asList(
                    "     &b&lAdmin Help",
                    "      &f┃ /alley reload &7- &bReloads the plugin.",
                    "      &f┃ /alley debug &7- &bDisplays info for development purposes.",
                    ""
            ).forEach(line -> sender.sendMessage(CC.translate(line)));
        }
    }
}