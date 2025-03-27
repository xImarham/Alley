package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionListCommand extends BaseCommand {
    @CommandData(name = "division.list", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        DivisionService divisionService = Alley.getInstance().getDivisionService();
        
        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lDivision List &f(" + divisionService.getDivisions().size() + "&f)"));
        if (divisionService.getDivisions().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Divisions available."));
        }
        divisionService.getDivisions()
                .forEach(division -> player.sendMessage(CC.translate("      &f● &b" + division.getDisplayName() + " &f(" + division.getTiers().get(0).getRequiredWins() + " wins)")));
        player.sendMessage("");

    }
}
