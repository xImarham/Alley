package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAListPlayersCommand extends BaseCommand {
    @Command(name = "ffa.listplayers", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /ffa listplayers <kit>"));
            return;
        }
        
        String kitName = args[0];
        AbstractFFAMatch match = Alley.getInstance().getFfaRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&l" + match.getKit().getDisplayName() + " Player List &f(" + match.getPlayers().size() + "&f)"));
        if (match.getPlayers().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Players available."));
        }
        match.getPlayers().forEach(participant -> player.sendMessage(CC.translate("      &f● &b" + participant.getName())));
        player.sendMessage("");
    }
}
