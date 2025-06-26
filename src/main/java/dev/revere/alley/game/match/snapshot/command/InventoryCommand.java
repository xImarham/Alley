package dev.revere.alley.game.match.snapshot.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.game.match.snapshot.SnapshotRepository;
import dev.revere.alley.game.match.snapshot.menu.InventorySnapshotMenu;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 22:19
 */
public class InventoryCommand extends BaseCommand {
    @CommandData(name = "inventory")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /inventory (uuid)"));
            return;
        }

        String uuid = args[0];
        Snapshot snapshot;
        try {
            snapshot = this.plugin.getSnapshotRepository().getSnapshot(UUID.fromString(uuid));
        } catch (Exception exception) {
            snapshot = this.plugin.getSnapshotRepository().getSnapshot(uuid);
        }

        new InventorySnapshotMenu(snapshot).openMenu(player);
    }
}