package dev.revere.alley.command.impl.other.gamemode;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 13/06/2025
 */
public class AdventureCommand extends BaseCommand {
    @CommandData(name = "gma", aliases = {"gm.a", "gamemode.a", "gm.2", "gm2", "gamemode.2", "gamemode.adventure"}, permission = "artex.command.gma")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(CC.translate("&eYour gamemode has been updated to Adventure."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        targetPlayer.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Adventure."));
        targetPlayer.sendMessage(CC.translate("&eYour gamemode has been updated to Adventure."));
    }
}