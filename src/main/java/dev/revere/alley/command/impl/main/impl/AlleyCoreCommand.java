package dev.revere.alley.command.impl.main.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.core.ICore;
import dev.revere.alley.core.enums.EnumCoreType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class AlleyCoreCommand extends BaseCommand {
    @CommandData(name = "alley.core", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        ICore core = this.plugin.getCoreAdapter().getCore();
        
        Arrays.asList(
            "",
            "&b&lCore Hook Information",
            " &f&l● &bPlugin: &f" + core.getType().getPluginName() + " &7made by &f" + core.getType().getPluginAuthor(),
            ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (core.getType() == EnumCoreType.DEFAULT) {
            sender.sendMessage(CC.translate("&7Note: This is the default core implementation, as there was no core found to hook into."));
        }
    }
}
