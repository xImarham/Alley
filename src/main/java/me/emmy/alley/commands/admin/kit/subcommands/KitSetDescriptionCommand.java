package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:46
 */

public class KitSetDescriptionCommand extends BaseCommand {
    @Override
    @Command(name = "kit.description", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        if (args.length() < 2) {
            sender.sendMessage(CC.translate("&cUsage: /kit description (kitName) (description)"));
            return;
        }

        String kitName = args.getArgs()[0];
        String description = args.getArgs()[1];

        Kit kit = Alley.getInstance().getKitManager().getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cThere is no kit with that name."));
            return;
        }

        kit.setDescription(description);
        Alley.getInstance().getKitManager().saveKit(kitName, kit);
        sender.sendMessage(CC.translate("&aSuccessfully set the description of the kit."));
    }
}
