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
 * Date: 22/05/2024 - 20:33
 */

public class PartyCreateCommand extends BaseCommand {
    @Override
    @Command(name = "party.create")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();

        if (partyRepository.getPartyLeader(playerUUID) != null) {
            player.sendMessage(CC.translate("&cYou're already in a party"));
            return;
        }

        if (partyRepository.getPartyMembers(playerUUID) != null) {
            player.sendMessage(CC.translate("&cYou're already in a party"));
            return;
        }

        partyRepository.createParty(playerUUID);
        Alley.getInstance().getHotbarUtility().applyPartyItems(player);
        player.sendMessage(CC.translate(ConfigLocale.PARTY_CREATED.getMessage()));
    }
}
