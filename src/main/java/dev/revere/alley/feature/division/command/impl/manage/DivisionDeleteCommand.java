package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionRepository;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionDeleteCommand extends BaseCommand {
    @Command(name = "division.delete", permission = "alley.admin", usage = "division delete <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/division delete &b<name>"));
            return;
        }
        
        String name = args[0];
        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        Division division = divisionRepository.getDivision(name);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }
        
        divisionRepository.deleteDivision(division.getName());
        player.sendMessage(CC.translate("&aSuccessfully deleted the division named &b" + name + "&a."));
    }
}
