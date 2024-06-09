package me.emmy.alley.party.command.impl.leader;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 22/05/2024 - 20:36
 */

public class PartyDisbandCommand extends BaseCommand {
    @Override
    @Command(name = "party.disband", aliases = {"p.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
            return;
        }

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();
        if (partyRepository.getPartyByLeader(player) != null) {
            partyRepository.disbandParty(player);
            player.sendMessage(CC.translate(Locale.PARTY_DISBANDED.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(Locale.NOT_LEADER.getMessage()));
    }
}
