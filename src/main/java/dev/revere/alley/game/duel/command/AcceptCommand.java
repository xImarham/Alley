package dev.revere.alley.game.duel.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.duel.DuelRequest;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:31
 */
public class AcceptCommand extends BaseCommand {
    @CommandData(name = "accept", aliases = {"duel.accept"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/accept &b<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        DuelRequest duelRequest = this.plugin.getDuelRequestService().getDuelRequest(player, target);
        if (duelRequest == null) {
            player.sendMessage(CC.translate("&cYou do not have a pending duel request from that player."));
            return;
        }

        if (this.plugin.getServerService().isQueueingEnabled(player)) {
            return;
        }

        this.plugin.getDuelRequestService().acceptPendingRequest(duelRequest);
        player.sendMessage(CC.translate("&aYou have accepted the duel request from " + target.getName() + "."));
    }
}
