package dev.revere.alley.profile.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.menu.shop.ShopMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class ShopCommand extends BaseCommand {
    @CommandData(name = "shop")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new ShopMenu().openMenu(player);
    }
}
