package me.emmy.alley.commands.global.party.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.party.Party;
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

        Party party = Alley.getInstance().getPartyRepository().getPartyLeader(playerUUID);

        // TODO: check if player is in party. If so, return.

        Alley.getInstance().getHotbarUtility().applyPartyItems(player);
        player.sendMessage(CC.translate(ConfigLocale.PARTY_CREATED.getMessage()));
    }
}
