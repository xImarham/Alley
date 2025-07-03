package dev.revere.alley.game.match.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class MatchResetBlocksCommand extends BaseCommand {
    @CommandData(name = "match.resetblocks", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        AbstractMatch match = profile.getMatch();
        if (match == null) {
            player.sendMessage(CC.translate("&cYou are not in a match!"));
            return;
        }

        match.resetBlockChanges();
        match.sendMessage(CC.translate("&4" + player.getName() + " &ffelt like being a nerd and reset the blocks!"));
    }
}
