package dev.revere.alley.feature.server.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.server.menu.ServiceMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceMenuCommand extends BaseCommand {
    @CommandData(name = "service.menu", permission = "alley.admin", usage = "/service menu", description = "Service menu command.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        
        new ServiceMenu().openMenu(player);
    }
}
