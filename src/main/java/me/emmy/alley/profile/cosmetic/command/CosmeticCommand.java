package me.emmy.alley.profile.cosmetic.command;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticCommand extends BaseCommand {
    @Command(name = "cosmetic", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(" ");
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("&d&lCosmetic Commands Help:"));
        player.sendMessage(CC.translate(" &f● &d/cosmetic list &7| List all cosmetics"));
        player.sendMessage(CC.translate(" &f● &d/cosmetic get &8(&7player&8)  &7| Get selected cosmetics"));
        player.sendMessage(CC.translate(" &f● &d/cosmetic set &8(&7player&8) &8(&7cosmetic&8)  &7| Set active cosmetic"));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");

    }
}
