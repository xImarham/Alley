package dev.revere.alley.base.queue.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.queue.menu.QueuesMenuModern;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/06/2025
 */
public class UnrankedCommand extends BaseCommand {
    @CommandData(name = "unranked")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new QueuesMenuModern().openMenu(player);
    }
}
