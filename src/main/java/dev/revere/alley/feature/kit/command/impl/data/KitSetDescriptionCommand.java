package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:46
 */
public class KitSetDescriptionCommand extends BaseCommand {
    @CommandData(name = "kit.description", aliases = "kit.setdesc",permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit description &b<kitName> <description>"));
            return;
        }

        String kitName = args[0];
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setDescription(description);
        Alley.getInstance().getKitRepository().saveKit(kit);
        sender.sendMessage(CC.translate(KitLocale.KIT_DESCRIPTION_SET.getMessage().replace("{kit-name}", kitName).replace("{kit-description}", description)));
    }
}