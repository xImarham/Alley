package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumChatChannel;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class PartyChatCommand extends BaseCommand {
    @Command(name = "party.chat", aliases = {"p.chat", "pc"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        String message = Arrays.stream(args).map(argument -> argument + " ").collect(Collectors.joining());

        if (args.length == 0) {
            if (profile.getProfileData().getProfileSettingData().getChatChannel().equals(EnumChatChannel.PARTY.toString())) {
                profile.getProfileData().getProfileSettingData().setChatChannel(EnumChatChannel.GLOBAL.toString());
                player.sendMessage(CC.translate("&aSet your chat channel to &bglobal&a."));
            } else {
                profile.getProfileData().getProfileSettingData().setChatChannel(EnumChatChannel.PARTY.toString());
                player.sendMessage(CC.translate("&aSet your chat channel to &bparty&a."));
            }
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
            return;
        }

        if (!profile.getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
            player.sendMessage(CC.translate(Locale.DISABLED_PARTY_CHAT.getMessage()));
            return;
        }

        Party party = Alley.getInstance().getPartyRepository().getPartyByMember(player.getUniqueId());

        profile.getParty().notifyParty(party.getChatFormat().replace("{player}", player.getName()).replace("{message}", message));
    }
}
