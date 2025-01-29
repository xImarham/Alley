package dev.revere.alley.cosmetic.command.impl.player;

import dev.revere.alley.cosmetic.menu.CosmeticsMenu;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticsCommand extends BaseCommand {
    @Command(name = "cosmetics")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new CosmeticsMenu("KillEffect").openMenu(player);
    }
}
