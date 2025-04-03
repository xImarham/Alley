package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.locale.PartyLocale;
import dev.revere.alley.profile.enums.EnumProfileState;
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

        PartyService partyService = Alley.getInstance().getPartyService();

        if (Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
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

        if (Alley.getInstance().getServerService().check(player)) {
            return;
        }

        partyService.createParty(player);
        player.sendMessage(CC.translate(PartyLocale.PARTY_CREATED.getMessage()));
    }
}