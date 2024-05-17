package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:26
 */

public class KitSaveCommand extends BaseCommand {
    @Override
    @Command(name = "kit.save", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        Alley.getInstance().getKitManager().saveKitsToFile();
        sender.sendMessage(CC.translate("&aSuccessfully saved all kits!"));
    }
}
