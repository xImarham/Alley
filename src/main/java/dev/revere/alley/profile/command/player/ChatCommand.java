package dev.revere.alley.profile.command.player;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumChatChannel;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 12:14
 */
public class ChatCommand extends BaseCommand {
    @CommandData(name = "chat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/chat &6<chat-channel>"));
            player.sendMessage(CC.translate("&cAvailable chat channels: " + EnumChatChannel.getChatChannelsSorted()));
            return;
        }

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (EnumChatChannel.getExactChatChannel(args[0], true) == null) {
            player.sendMessage(CC.translate("&cThe chat channel &6" + args[0] + " &cdoes not exist."));
            return;
        }

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(args[0])) {
            player.sendMessage(CC.translate("&cYou're already in the " + args[0] + " chat channel."));
            return;
        }

        profile.getProfileData().getSettingData().setChatChannel(EnumChatChannel.getExactChatChannel(args[0], true));
        player.sendMessage(CC.translate("&aSet your chat channel to &6" + args[0] + "&a."));
    }
}