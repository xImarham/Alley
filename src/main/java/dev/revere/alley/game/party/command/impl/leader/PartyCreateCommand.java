package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.feature.server.IServerService;
import dev.revere.alley.game.party.IPartyService;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.profile.progress.IProgressService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:33
 */
public class PartyCreateCommand extends BaseCommand {
    @Override
    @CommandData(name = "party.create", aliases = {"p.create"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        IPartyService partyService = Alley.getInstance().getService(IPartyService.class);

        if (Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (partyService.getPartyByLeader(player) != null) {
            player.sendMessage(CC.translate(PartyLocale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        if (partyService.getPartyByMember(playerUUID) != null) {
            player.sendMessage(CC.translate(PartyLocale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        if (Alley.getInstance().getService(IServerService.class).isQueueingAllowed()) {
            return;
        }

        partyService.createParty(player);
        //player.sendMessage(CC.translate(PartyLocale.PARTY_CREATED.getMessage()));
    }
}