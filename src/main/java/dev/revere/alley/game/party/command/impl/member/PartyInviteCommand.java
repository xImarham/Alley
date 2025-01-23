package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
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
            player.sendMessage(CC.translate("&cThe player you are trying to invite is not online."));
            return;
        }

        if (targetPlayer == command.getPlayer()) {
            player.sendMessage(CC.translate("&cYou cannot invite yourself to a party."));
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

        Alley.getInstance().getPartyHandler().sendInvite(party, player, targetPlayer);
        party.notifyParty("&b" + targetPlayer.getName() + " &awas invited to the party by &b" + player.getName() + "&a.");
    }
}