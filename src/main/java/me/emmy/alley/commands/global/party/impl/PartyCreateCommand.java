package me.emmy.alley.commands.global.party.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.profile.Profile;
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
    @Command(name = "party.create", aliases = {"p.create"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(playerUUID);
        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();

        if (partyRepository.getPartyByLeader(player) != null) {
            player.sendMessage(CC.translate("&cYou're already in a party"));
            return;
        }

        if (partyRepository.getPartyByMember(playerUUID) != null) {
            player.sendMessage(CC.translate("&cYou're already in a party"));
            return;
        }

        partyRepository.createParty(player);
        player.sendMessage(CC.translate(ConfigLocale.PARTY_CREATED.getMessage()));
    }
}
