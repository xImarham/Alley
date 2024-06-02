package me.emmy.alley.profile.division.command;

import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionCommand extends BaseCommand {
    @Command(name = "division", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(" ");
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("&d&lDivision Commands Help:"));
        player.sendMessage(CC.translate(" &f● &d/division list &7| View all divisions"));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage("");
    }
}
