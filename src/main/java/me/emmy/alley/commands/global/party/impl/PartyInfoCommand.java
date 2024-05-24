package me.emmy.alley.commands.global.party.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.party.Party;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 24/05/2024 - 19:17
 */

public class PartyInfoCommand extends BaseCommand {
    @Override
    @Command(name = "party.info")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (Party party : Alley.getInstance().getPartyRepository().getParties()) {
            player.sendMessage(CC.translate(""));
            player.sendMessage(CC.translate("&bLeader: &f" + Alley.getInstance().getPartyRepository().getPartyLeader(party.getLeader())));
            player.sendMessage(CC.translate("&bMembers &f" + player.getName())); //TODO: get actual members "party.getMembers"
            player.sendMessage(CC.translate(""));
        }
    }
}
