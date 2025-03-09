package dev.revere.alley.feature.service.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.service.command.impl.ServiceAllowQueueCommand;
import dev.revere.alley.feature.service.command.impl.ServiceMenuCommand;
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

    @CommandData(name = "service", permission = "alley.admin", usage = "/service", description = "Service command.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&b&lService Commands",
                " &f● &b/service menu &7| &fOpens the service menu.",
                " &f● &b/service allowqueue &8<&7true/false&8> &7| &fAllow/disallow queueing.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}