package dev.revere.alley.profile.command;

import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:03
 */
public class ChallengesCommand extends BaseCommand {
    @CommandData(name = "challenges")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&cThis feature is not yet implemented."));
    }
}