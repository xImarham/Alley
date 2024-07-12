package me.emmy.alley.party.command.impl.leader;

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
