package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
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

        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn in order to execute this command :v"));
            return;
        }

        if (partyHandler.getPartyByLeader(player) != null) {
            player.sendMessage(CC.translate(Locale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        if (partyHandler.getPartyByMember(playerUUID) != null) {
            player.sendMessage(CC.translate(Locale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        partyHandler.createParty(player);
        player.sendMessage(CC.translate(Locale.PARTY_CREATED.getMessage()));
    }
}