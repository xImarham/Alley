package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 22/05/2024 - 20:36
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

        player.sendMessage(CC.translate("&cYou're not in a party"));
    }
}
