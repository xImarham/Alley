package dev.revere.alley.division.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.division.DivisionRepository;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionListCommand extends BaseCommand {
    @Command(name = "division.list", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        
        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lDivision List &f(" + divisionRepository.getDivisions().size() + "&f)"));
        if (divisionRepository.getDivisions().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Divisions available."));
        }
        divisionRepository.getDivisions()
                .forEach(division -> player.sendMessage(CC.translate("      &f● &b" + division.getDisplayName() + " &f(" + division.getTiers().get(0).getRequiredWins() + " wins)")));
        player.sendMessage("");

    }
}
