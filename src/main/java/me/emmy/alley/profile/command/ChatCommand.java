package me.emmy.alley.profile.command;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumChatChannel;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 12:14
 */
public class ChatCommand extends BaseCommand {
    @Command(name = "chat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/chat &b<chat-channel>"));
            player.sendMessage(CC.translate("&cAvailable chat channels: " + EnumChatChannel.getChatChannelsSorted()));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (EnumChatChannel.getExactChatChannel(args[0], true) == null) {
            player.sendMessage(CC.translate("&cThe chat channel &b" + args[0] + " &cdoes not exist."));
            return;
        }

        if (profile.getProfileData().getProfileSettingData().getChatChannel().equalsIgnoreCase(args[0])) {
            player.sendMessage(CC.translate("&cYou're already in the " + args[0] + " chat channel."));
            return;
        }

        profile.getProfileData().getProfileSettingData().setChatChannel(EnumChatChannel.getExactChatChannel(args[0], true));
        player.sendMessage(CC.translate("&aSet your chat channel to &b" + args[0] + "&a."));
    }
}