package dev.revere.alley.profile.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.menu.match.MatchHistorySelectKitMenu;
import dev.revere.alley.profile.menu.match.button.MatchHistorySelectKitButton;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:05
 */
public class MatchHistoryCommand extends BaseCommand {
    @CommandData(name = "matchhistory")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new MatchHistorySelectKitMenu().openMenu(player);
    }
}