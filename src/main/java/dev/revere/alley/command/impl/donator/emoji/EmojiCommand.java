package dev.revere.alley.command.impl.donator.emoji;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.command.impl.donator.emoji.impl.EmojiListCommand;
import dev.revere.alley.essential.chat.enums.EnumEmojiType;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.ClickableUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 22/01/2025 - 21:15
 */
public class EmojiCommand extends BaseCommand {

    public EmojiCommand() {
        new EmojiListCommand();
    }

    @Command(name = "emoji", aliases = "emojis", permission = "alley.donator.chat.symbol", usage = "/emoji", description = "Help guide for emojis")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&b&lEmoji Help",
                " &7To use an emoji, simply type the identifier in chat.",
                " &7For example, if you want to use the &f" + EnumEmojiType.HEART.getFormat() + " &7emoji, type &f" + EnumEmojiType.HEART.getIdentifier() + "&7 in chat.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        player.spigot().sendMessage(ClickableUtil.createComponent(" &a[Click here to view all emojis]", "/emoji list", "&7Click here to view a list of all available emojis."));
        player.sendMessage("");
    }
}