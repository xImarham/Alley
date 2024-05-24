package me.emmy.alley.commands.global.party.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.party.Party;
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
    @Command(name = "party.leave")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();

        if (partyRepository.getPartyLeader(playerUUID) != null) {
            partyRepository.disbandParty(playerUUID);
            player.sendMessage(CC.translate(ConfigLocale.PARTY_DISBANDED.getMessage()));
            Alley.getInstance().getHotbarUtility().applySpawnItems(player);
            return;
        }

        if (partyRepository.getPartyMembers(playerUUID) != null) {
            partyRepository.leaveParty(playerUUID);
            player.sendMessage(CC.translate(ConfigLocale.PARTY_LEFT.getMessage()));
            Alley.getInstance().getHotbarUtility().applySpawnItems(player);
            return;
        }

        player.sendMessage(CC.translate("&cYou're not in a partyyyyyyyyyyyyyyyyyyyyyyy like damnnnn"));
    }
}
