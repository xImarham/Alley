package dev.revere.alley.division.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.annotation.Completer;
import dev.revere.alley.division.Division;
import dev.revere.alley.division.DivisionRepository;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 28/01/2025
 */
public class DivisionSetWinsCommand extends BaseCommand {

    @Completer(name = "division.setwins")
    public List<String> DivisionSetWinsCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission("alley.admin")) {
            Alley.getInstance().getDivisionRepository().getDivisions().forEach(division -> completion.add(division.getName()));
        } else if (command.getArgs().length == 2 && command.getPlayer().hasPermission("alley.admin")) {
            Division division = Alley.getInstance().getDivisionRepository().getDivision(command.getArgs()[0]);
            if (division != null) {
                division.getTiers().forEach(tier -> completion.add(tier.getName()));
            }
        }

        return completion;
    }

    @Command(name = "division.setwins", permission = "alley.admin", usage = "division setwins <name> <tier> <wins>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&6Usage: &e/division setwins &b<name> <tier> <wins>"));
            return;
        }

        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        Division division = divisionRepository.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        String tier = args[1];
        if (division.getTier(tier) == null) {
            player.sendMessage(CC.translate("&cThe " + division.getDisplayName() + " division does not have a tier named " + tier + "."));
            return;
        }

        int wins;
        try {
            wins = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (wins < 0) {
            player.sendMessage(CC.translate("&cThe number of wins can't be 0."));
            return;
        }

        division.getTier(tier).setRequiredWins(wins);
        divisionRepository.saveDivision(division);
        player.sendMessage(CC.translate("&aSuccessfully set the required wins for the " + division.getDisplayName() + " division's " + tier + " tier to " + wins + "."));
    }
}