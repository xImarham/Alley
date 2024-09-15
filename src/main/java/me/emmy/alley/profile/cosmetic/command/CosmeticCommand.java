package me.emmy.alley.profile.cosmetic.command;

import me.emmy.alley.profile.cosmetic.command.impl.admin.CosmeticGetSelectedCommand;
import me.emmy.alley.profile.cosmetic.command.impl.admin.CosmeticListCommand;
import me.emmy.alley.profile.cosmetic.command.impl.admin.CosmeticSetCommand;
import me.emmy.alley.profile.cosmetic.command.impl.player.CosmeticsCommand;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticCommand extends BaseCommand {

    /**
     * Register all Cosmetic subcommands in the constructor
     */
    public CosmeticCommand() {
        new CosmeticsCommand();
        new CosmeticListCommand();
        new CosmeticSetCommand();
        new CosmeticGetSelectedCommand();
    }

    @Command(name = "cosmetic", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(" ");
        player.sendMessage(CC.translate("&b&lCosmetic Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/cosmetic list &7| List all cosmetics"));
        player.sendMessage(CC.translate(" &f● &b/cosmetic get &8(&7player&8)  &7| Get selected cosmetics"));
        player.sendMessage(CC.translate(" &f● &b/cosmetic set &8(&7player&8) &8(&7cosmetic&8)  &7| Set active cosmetic"));
        player.sendMessage("");

    }
}
