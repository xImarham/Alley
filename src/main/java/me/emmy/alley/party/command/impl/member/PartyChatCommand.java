package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.Party;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumChatChannel;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
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
