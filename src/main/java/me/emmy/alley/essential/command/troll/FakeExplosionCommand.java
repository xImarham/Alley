package me.emmy.alley.essential.command.troll;

import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 11:51
 */
public class FakeExplosionCommand extends BaseCommand {
    @Override
    @Command(name = "fakeexplosion", permission = "delta.command.fakeexplosion", usage = "fakeexplosion", description = "Fake an explosion")
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