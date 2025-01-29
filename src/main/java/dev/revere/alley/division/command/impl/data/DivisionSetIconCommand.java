package dev.revere.alley.division.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.division.Division;
import dev.revere.alley.division.DivisionRepository;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetIconCommand extends BaseCommand {
    @Command(name = "division.seticon", permission = "alley.admin", usage = "division seticon <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/division seticon &b<name>"));
            return;
        }

        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        Division division = divisionRepository.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        if (player.getItemInHand() == null) {
            player.sendMessage(CC.translate("&cYou need be holding an item to set it as the division icon."));
            return;
        }

        division.setIcon(player.getItemInHand().getType());
        division.setDurability(player.getItemInHand().getDurability());
        divisionRepository.saveDivision(division);
        player.sendMessage(CC.translate("&aSuccessfully set the icon for the division &b" + division.getDisplayName() + " &ato " + player.getItemInHand().getType().name() + ":" + player.getItemInHand().getDurability() + "&a."));
    }
}