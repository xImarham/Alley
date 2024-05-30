package me.emmy.alley.tournament.command.player;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:54
 */
public class TournamentCommand extends BaseCommand {
    @Override
    @Command(name = "tournament")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("bla bla bla yap yap yap"));
        player.sendMessage(CC.translate(" i was too lazy"));
        player.sendMessage(CC.translate(" i dont have copilot"));
        player.sendMessage(CC.translate(" i considered buying it"));
    }
}
