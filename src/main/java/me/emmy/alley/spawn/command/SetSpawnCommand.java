package me.emmy.alley.spawn.command;

import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 29/04/2024 - 18:41
 */
public class SetSpawnCommand extends BaseCommand {

    @Override
    @Command(name = "setspawn", permission = "alley.admin")
    public void onCommand(CommandArgs cmd) {
        Player player = (Player) cmd.getSender();
        Location location = player.getLocation();
        Alley.getInstance().getSpawnHandler().setSpawnLocation(location);

        String message = ConfigHandler.getInstance().getMessagesConfig().getString("spawn.spawn-set");
        message = message.replace("{world}", location.getWorld().getName())
                .replace("{x}", String.format("%.2f", location.getX()))
                .replace("{y}", String.format("%.2f", location.getY()))
                .replace("{z}", String.format("%.2f", location.getZ()))
                .replace("{yaw}", String.format("%.2f", location.getYaw()))
                .replace("{pitch}", String.format("%.2f", location.getPitch()));

        player.sendMessage(CC.translate(message));
    }
}