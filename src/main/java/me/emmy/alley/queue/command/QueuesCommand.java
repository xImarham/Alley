package me.emmy.alley.queue.command;

import me.emmy.alley.queue.menu.queues.QueuesMenu;
import me.emmy.alley.queue.menu.unranked.UnrankedMenu;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 16:45
 */

public class QueuesCommand extends BaseCommand {
    @Override
    @Command(name = "queues", aliases = {"selectqueue"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new QueuesMenu().openMenu(player);
    }
}
