package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyLeaveCommand extends BaseCommand {
    @Override
    @Command(name = "party.leave", aliases = {"p.leave"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();

        if (partyRepository.getPartyByLeader(player) != null) {
            partyRepository.disbandParty(player);
            player.sendMessage(CC.translate(Locale.PARTY_DISBANDED.getMessage()));
            return;
        }

        if (partyRepository.getPartyByMember(playerUUID) != null) {
            partyRepository.leaveParty(player);
            player.sendMessage(CC.translate(Locale.PARTY_LEFT.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
    }
}
