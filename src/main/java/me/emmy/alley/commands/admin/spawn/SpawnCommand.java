package me.emmy.alley.commands.admin.spawn;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 29/04/2024 - 19:01
 */

public class SpawnCommand extends BaseCommand {
    @Override
    @Command(name = "spawn", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        PlayerUtil.reset(player);
        PlayerUtil.teleportToSpawn(player);
        Alley.getInstance().getHotbarUtility().applySpawnItems(player);
        player.sendMessage(CC.translate(Alley.getInstance().getConfig("messages.yml").getString("spawn.teleported")));
    }
}
