package dev.revere.alley.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.party.PartyRepository;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:33
 */
public class PartyCreateCommand extends BaseCommand {
    @Override
    @Command(name = "party.create", aliases = {"p.create"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (partyRepository.getPartyByLeader(player) != null) {
            player.sendMessage(CC.translate(Locale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        if (partyRepository.getPartyByMember(playerUUID) != null) {
            player.sendMessage(CC.translate(Locale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        partyRepository.createParty(player);
        player.sendMessage(CC.translate(Locale.PARTY_CREATED.getMessage()));
    }
}
