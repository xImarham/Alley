package dev.revere.alley.profile.shop.command;

import dev.revere.alley.profile.shop.menu.ShopMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
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
