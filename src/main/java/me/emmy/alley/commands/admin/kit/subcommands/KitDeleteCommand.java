package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:19
 */

public class KitDeleteCommand extends BaseCommand {
    @Override
    @Command(name = "kit.delete", permission = "practice.admin", inGameOnly = false)
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        if (args.length() < 1) {
            sender.sendMessage(CC.translate("&cUsage: /kit delete (kitName)"));
            return;
        }

        String kitName = args.getArgs()[0];

        Kit kit = Alley.getInstance().getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(CC.translate("&cNo kit named " + kitName + " &cwas found!"));
            return;
        }

        Alley.getInstance().getKitManager().getKits().remove(kit);
        Alley.getInstance().getKitManager().saveKit(kitName, kit);
        sender.sendMessage(CC.translate("&cSuccessfully deleted the &4" + kitName + " &ckit!"));
    }
}
