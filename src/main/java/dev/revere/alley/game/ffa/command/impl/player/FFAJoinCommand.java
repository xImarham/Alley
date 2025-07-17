package dev.revere.alley.game.ffa.command.impl.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.ffa.IFFAService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAJoinCommand extends BaseCommand {
    @CommandData(name = "ffa.join")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa join &6<kit>"));
            return;
        }

        String kitName = args[0];
        Kit kit = this.plugin.getService(IKitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage("Kit not found.");
            return;
        }

        IProfileService profileService = this.plugin.getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() != null) {
            player.sendMessage(CC.translate("&cYou must leave your party to join FFA."));
            return;
        }

        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou can only join FFA from the lobby."));
            return;
        }

        IFFAService ffaService = this.plugin.getService(IFFAService.class);
        ffaService.getMatches().stream()
                .filter(match -> match.getKit().equals(kit))
                .filter(match -> match.getPlayers().size() < match.getMaxPlayers())
                .findFirst()
                .ifPresent(match -> match.join(player));
    }
}