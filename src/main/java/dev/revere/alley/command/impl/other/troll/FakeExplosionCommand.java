package dev.revere.alley.command.impl.other.troll;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/06/2024 - 11:51
 */
public class FakeExplosionCommand extends BaseCommand {
    @Override
    @CommandData(name = "fakeexplosion", permission = "alley.command.troll.fakeexplosion", usage = "fakeexplosion", description = "Fake an explosion")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/fakeexplosion &b<player>"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        targetPlayer.getWorld().createExplosion(targetPlayer.getLocation(), 0.0F, false);
        player.sendMessage(CC.translate("&bYou've fake exploded &7" + targetPlayer.getName()));
    }
}