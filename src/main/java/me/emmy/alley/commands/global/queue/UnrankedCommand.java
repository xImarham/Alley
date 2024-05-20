package me.emmy.alley.commands.global.queue;

import me.emmy.alley.locale.Locale;
import me.emmy.alley.menus.unranked.UnrankedMenu;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/05/2024 - 11:30
 */

public class UnrankedCommand extends BaseCommand {
    @Override
    @Command(name = "unranked")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new UnrankedMenu().openMenu(player);
    }
}
