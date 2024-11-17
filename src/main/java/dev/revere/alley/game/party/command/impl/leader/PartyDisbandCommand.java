package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyDisbandCommand extends BaseCommand {
    @Override
    @Command(name = "party.disband", aliases = {"p.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
            return;
        }

        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
        if (partyHandler.getPartyByLeader(player) != null) {
            partyHandler.disbandParty(player);
            player.sendMessage(CC.translate(Locale.PARTY_DISBANDED.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(Locale.NOT_LEADER.getMessage()));
    }
}
