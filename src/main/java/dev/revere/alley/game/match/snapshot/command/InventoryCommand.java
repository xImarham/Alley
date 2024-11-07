package dev.revere.alley.game.match.snapshot.command;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.game.match.snapshot.SnapshotRepository;
import dev.revere.alley.game.match.snapshot.menu.InventorySnapshotMenu;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 22:19
 */
public class InventoryCommand extends BaseCommand {
    @Command(name = "inventory")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() != 1) {
            player.sendMessage(CC.translate("&cUsage: /inventory (player)"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null) {
            player.sendMessage(ErrorMessage.PLAYER_NOT_ONLINE.replace("{player}", args[0]));
            return;
        }

        SnapshotRepository snapshotRepository = Alley.getInstance().getSnapshotRepository();
        if (snapshotRepository.getSnapshot(targetPlayer.getUniqueId()) == null) {
            player.sendMessage(CC.translate("&cYou cannot view that player's inventory any longer."));
            return;
        }

        new InventorySnapshotMenu(targetPlayer.getUniqueId()).openMenu(player);
    }
}