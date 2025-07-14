package dev.revere.alley.base.server.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.impl.ServiceAllowQueueCommand;
import dev.revere.alley.base.server.impl.ServiceMenuCommand;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceCommand extends BaseCommand {

    public ServiceCommand() {
        new ServiceAllowQueueCommand();
        new ServiceMenuCommand();
    }

    @CommandData(name = "service", isAdminOnly = true, usage = "/service", description = "Service command.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&6&lService Commands",
                " &f● &6/service menu &7| &fOpens the service menu.",
                " &f● &6/service allowqueue &8(&7true/false&8) &7| &fAllow/disallow queueing.",
                " &f● &6/service disablecrafting &7| &fDisable crafting for an item.",
                " &f● &6/service enablecrafting &7| &fEnable crafting for an item.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}