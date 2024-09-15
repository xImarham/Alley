package me.emmy.alley.profile.cosmetic.command.impl.player;

import me.emmy.alley.profile.cosmetic.menu.CosmeticsMenu;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
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
