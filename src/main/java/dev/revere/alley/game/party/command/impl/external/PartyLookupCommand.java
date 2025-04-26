package dev.revere.alley.game.party.command.impl.external;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 23/01/2025
 */
public class PartyLookupCommand extends BaseCommand {
    @CommandData(name = "party.lookup", aliases = {"pl"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/party lookup &b<player>"));
            return;
        }

        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cInvalid player."));
            return;
        }

        Party party = this.plugin.getPartyService().getParty(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis player is not in a party."));
            return;
        }

        Arrays.asList(
            "&b&l" + party.getLeader().getName() + "'s Party",
            " &f● &bLeader: &f" + party.getLeader().getName(),
            " &f● &bMembers: &f" + party.getMembers().size(),
            " &f● &bStatus: &f" + (party.getState().getName()),
            " &f● &bPrivacy: &f" + (party.getState().getDescription()
            )).forEach(msg -> player.sendMessage(CC.translate(msg)));
    }
}