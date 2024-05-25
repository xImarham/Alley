package me.emmy.alley.queue.command;

import me.emmy.alley.queue.menu.ranked.RankedMenu;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/05/2024 - 11:31
 */

public class RankedCommand extends BaseCommand {
    @Override
    @Command(name = "ranked")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new RankedMenu().openMenu(player);
    }
}
