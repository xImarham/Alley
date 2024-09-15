package me.emmy.alley.profile.shop.command;

import me.emmy.alley.profile.shop.menu.ShopMenu;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class ShopCommand extends BaseCommand {
    @Command(name = "shop")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new ShopMenu().openMenu(player);
    }
}
