package me.emmy.alley.match.snapshot.command;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.match.snapshot.SnapshotRepository;
import me.emmy.alley.match.snapshot.menu.InventorySnapshotMenu;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 22:19
 */
public class InventoryCommand extends BaseCommand {
    @Override
    @Command(name = "inventory")
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

        new InventorySnapshotMenu(targetPlayer).openMenu(player);
    }
}
