package me.emmy.alley.spawn.command;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: FlowerCore
 * Discord: dsc.gg/emmiesa
 */

public class SetSpawnCommand extends BaseCommand {

    @Command(name = "setspawn", permission = "plugin.owner")
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = (Player) cmd.getSender();
        Location loc = player.getLocation();
        Alley.getInstance().getSpawnHandler().setSpawnLocation(loc);

        String message = Alley.getInstance().getConfig("messages.yml").getString("spawn.spawn-set");
        message = message.replace("{world}", loc.getWorld().getName())
                .replace("{x}", String.format("%.2f", loc.getX()))
                .replace("{y}", String.format("%.2f", loc.getY()))
                .replace("{z}", String.format("%.2f", loc.getZ()))
                .replace("{yaw}", String.format("%.2f", loc.getYaw()))
                .replace("{pitch}", String.format("%.2f", loc.getPitch()));

        player.sendMessage(CC.translate(message));
    }
}