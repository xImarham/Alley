package me.emmy.alley.queue.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueReloadCommand extends BaseCommand {
    @Command(name = "queue.reload", permission = "alley.admin")
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
