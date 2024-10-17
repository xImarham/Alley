package me.emmy.alley.game.duel.command;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.game.duel.DuelRequest;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:31
 */
public class AcceptCommand extends BaseCommand {
    @Command(name = "accept", aliases = {"duel.accept"})
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

        DuelRequest duelRequest = Alley.getInstance().getDuelRepository().getDuelRequest(player, target);
        if (duelRequest == null) {
            player.sendMessage(CC.translate("&cYou do not have a pending duel request from that player."));
            return;
        }

        Alley.getInstance().getDuelRepository().acceptPendingRequest(duelRequest);
        player.sendMessage(CC.translate("&aYou have accepted the duel request from " + target.getName() + "."));
    }
}
