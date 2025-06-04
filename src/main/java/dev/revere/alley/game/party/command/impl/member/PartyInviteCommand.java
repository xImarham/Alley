package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.Party;
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
    @CommandData(name = "party.invite", aliases = "p.invite")
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

        Party party = this.plugin.getPartyService().getPartyByMember(player.getUniqueId());
        if (party == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        Profile targetProfile = this.plugin.getProfileService().getProfile(targetPlayer.getUniqueId());
        if (!targetProfile.getProfileData().getSettingData().isPartyInvitesEnabled()) {
            player.sendMessage(CC.translate(PartyLocale.PLAYER_DISABLED_PARTY_INVITES.getMessage().replace("{player}", target)));
            return;
        }

        if (party.getMembers().contains(targetPlayer.getUniqueId())) {
            player.sendMessage(CC.translate("&b" + targetPlayer.getName() + " &cis already in your party."));
            return;
        }

        this.plugin.getPartyService().sendInvite(party, player, targetPlayer);
        party.notifyParty("&b" + targetPlayer.getName() + " &awas invited to the party by &b" + player.getName() + "&a.");
    }
}