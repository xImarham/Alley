package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.party.menu.event.PartyEventMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class PartyEventMenuCommand extends BaseCommand {
    @CommandData(name = "party.event")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new PartyEventMenu().openMenu(player);
    }
}
