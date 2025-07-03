package dev.revere.alley.game.ffa.command.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.ffa.IFFAService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAListCommand extends BaseCommand {
    @CommandData(name = "ffa.list", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        IFFAService ffaService = Alley.getInstance().getService(IFFAService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &6&lFFA Match List &f(" + ffaService.getMatches().size() + "&f)"));
        if (ffaService.getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Matches available."));
        }
        ffaService.getMatches().forEach(match -> player.sendMessage(CC.translate("      &f● &6" + match.getKit().getDisplayName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage("");
    }
}
