package dev.revere.alley.queue.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueReloadCommand extends BaseCommand {
    @Command(name = "queue.reload", aliases = {"reloadqueue", "reloadqueues"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Alley.getInstance().getQueueRepository().getQueues().clear();
        Alley.getInstance().getKitRepository().getKits().forEach(kit -> {
            if (!kit.isEnabled()) return;
            new Queue(kit, false);

            if (kit.isSettingEnabled(KitSettingRankedImpl.class)) {
                new Queue(kit, true);
            }
        });

        player.sendMessage(CC.translate("&aYou've reloaded the queues."));

    }
}
