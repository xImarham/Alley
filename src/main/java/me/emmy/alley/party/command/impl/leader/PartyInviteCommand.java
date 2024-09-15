package me.emmy.alley.party.command.impl.leader;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.Party;
import me.emmy.alley.party.PartyRequest;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
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

        Party party = Alley.getInstance().getPartyRepository().getPartyByMember(player.getUniqueId());
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
        Alley.getInstance().getPartyRepository().addRequest(request);
        request.sendRequest(party, targetPlayer);
    }
}
