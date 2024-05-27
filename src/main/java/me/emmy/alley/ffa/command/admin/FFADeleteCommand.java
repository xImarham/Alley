package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.ffa.impl.DefaultFFAMatchImpl;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.Locale;
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
public class FFADeleteCommand extends BaseCommand {
    @Command(name = "ffa.delete", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /ffa delete <kit>"));
            return;
        }

        String kitName = args[0];
        AbstractFFAMatch match = Alley.getInstance().getFfaRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        Alley.getInstance().getFfaRepository().deleteFFAMatch(match);
        player.sendMessage(CC.translate("&aSuccessfully deleted the FFA match."));
    }
}
