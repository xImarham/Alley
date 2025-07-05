package dev.revere.alley.game.duel.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.feature.server.IServerService;
import dev.revere.alley.game.duel.menu.DuelRequestsMenu;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 18:19
 */
public class DuelRequestsCommand extends BaseCommand {
    @CommandData(name = "duelrequests", aliases = {"viewduelrequests", "viewrequests"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
            player.sendMessage(CC.translate("&cYou are already in a match."));
            return;
        }

        IServerService serverService = Alley.getInstance().getService(IServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cQueueing is temporarily disabled. Please try again later."));
            player.closeInventory();
            return;
        }

        new DuelRequestsMenu().openMenu(player);
    }
}