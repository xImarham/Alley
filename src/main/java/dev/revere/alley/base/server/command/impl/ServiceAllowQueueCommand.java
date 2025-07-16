package dev.revere.alley.base.server.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.IServerService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceAllowQueueCommand extends BaseCommand {
    @CommandData(name = "service.allowqueue", isAdminOnly = true, usage = "/service allowqueue <true/false>", description = "Allow/disallow queueing.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/service allowqueue &6<true/false>"));
            return;
        }

        boolean allowQueue;
        try {
            allowQueue = Boolean.parseBoolean(args[0]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid parameter. Please use true or false."));
            return;
        }

        IServerService serverService = this.plugin.getService(IServerService.class);
        serverService.clearAllQueues(player);
        serverService.setQueueingAllowed(allowQueue);
        player.sendMessage(CC.translate("&aYou've " + (allowQueue ? "&aenabled" : "&cdisabled") + " queueing."));
    }
}