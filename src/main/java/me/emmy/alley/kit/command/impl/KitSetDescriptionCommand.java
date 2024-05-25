package me.emmy.alley.kit.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:46
 */

public class KitSetDescriptionCommand extends BaseCommand {
    @Override
    @Command(name = "kit.description", aliases = "kit.setdesc",permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            sender.sendMessage(CC.translate("&cUsage: /kit description (kitName) (description)"));
            return;
        }

        String kitName = args[0];
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setDescription(description);
        Alley.getInstance().getKitRepository().saveKit(kit);
        sender.sendMessage(CC.translate(Locale.KIT_DESCRIPTION_SET.getMessage().replace("{kit-name}", kitName).replace("{kit-description}", description)));
    }
}
