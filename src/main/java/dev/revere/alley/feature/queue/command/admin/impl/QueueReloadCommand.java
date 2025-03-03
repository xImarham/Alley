package dev.revere.alley.feature.queue.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueReloadCommand extends BaseCommand {
    @CommandData(name = "queue.reload", aliases = {"reloadqueue", "reloadqueues"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Alley.getInstance().getQueueRepository().reloadQueues();
        player.sendMessage(CC.translate("&aYou've reloaded the queues."));
    }
}