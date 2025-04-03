package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.locale.PartyLocale;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyDisbandCommand extends BaseCommand {
    @Override
    @CommandData(name = "party.disband", aliases = {"p.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());

        if (Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        PartyService partyService = Alley.getInstance().getPartyService();
        if (partyService.getPartyByLeader(player) != null) {
            partyService.disbandParty(player);
            player.sendMessage(CC.translate(PartyLocale.PARTY_DISBANDED.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(PartyLocale.NOT_LEADER.getMessage()));
    }
}
