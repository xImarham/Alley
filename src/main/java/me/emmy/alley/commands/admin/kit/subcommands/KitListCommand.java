package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:07
 */

public class KitListCommand extends BaseCommand {
    @Override
    @Command(name = "kit.list", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(CC.translate("     &b&lKit list"));
        Alley.getInstance().getKitManager().getKits().forEach(kit -> sender.sendMessage(CC.translate("      &f● &b" + kit.getDisplayName())));
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage("");
    }
}
