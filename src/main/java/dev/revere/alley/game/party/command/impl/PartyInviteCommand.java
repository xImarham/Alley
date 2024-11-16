package dev.revere.alley.game.party.command.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.game.party.PartyRequest;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:27
 */
public class PartyInviteCommand extends BaseCommand {
    @Override
    @Command(name = "party.invite", aliases = "p.invite")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /party invite (player)"));
            return;
        }

        String target = args[0];
        Player targetPlayer = Bukkit.getPlayer(target);

        if (targetPlayer == null) {
            player.sendMessage(CC.translate(ErrorMessage.PLAYER_NOT_ONLINE.replace("{player}", target)));
            return;
        }

        if (targetPlayer == command.getPlayer()) {
            player.sendMessage(CC.translate(ErrorMessage.CANNOT_INVITE_SELF));
            return;
        }

        Party party = Alley.getInstance().getPartyHandler().getPartyByMember(player.getUniqueId());
        if (party == null) {
            player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
            return;
        }

        Profile targetProfile = Alley.getInstance().getProfileRepository().getProfile(targetPlayer.getUniqueId());
        if (!targetProfile.getProfileData().getProfileSettingData().isPartyInvitesEnabled()) {
            player.sendMessage(CC.translate(Locale.PLAYER_DISABLED_PARTY_INVITES.getMessage().replace("{player}", target)));
            return;
        }

        PartyRequest request = new PartyRequest(player, targetPlayer);
        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
        partyHandler.addRequest(request);
        partyHandler.sendRequest(party, targetPlayer);
    }
}
