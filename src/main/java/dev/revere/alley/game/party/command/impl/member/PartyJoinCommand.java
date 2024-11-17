package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.enums.EnumPartyState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 23:25
 */
public class PartyJoinCommand extends BaseCommand {
    @Command(name = "party.join", aliases = {"p.join"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/party join &b<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        Party party = Alley.getInstance().getPartyHandler().getPartyByLeader(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cThat player is not in a party."));
            return;
        }

        if (party.getState() != EnumPartyState.PUBLIC) {
            player.sendMessage(CC.translate("&cThat party is not open to the public."));
            return;
        }

        Alley.getInstance().getPartyHandler().joinParty(player, target);
    }
}