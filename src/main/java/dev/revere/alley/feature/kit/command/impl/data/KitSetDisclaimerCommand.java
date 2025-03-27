package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:41
 */
public class KitSetDisclaimerCommand extends BaseCommand {
    @CommandData(name = "kit.setdisclaimer", permission = "alley.kit.setdisclaimer", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setdisclaimer &b<kitName> <disclaimer>"));
            return;
        }

        KitService kitService = Alley.getInstance().getKitService();
        String kitName = args[0];
        String disclaimer = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (kitService.getKit(kitName) == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kitService.getKit(kitName).setDisclaimer(disclaimer);
        kitService.saveKit(kitService.getKit(kitName));
        sender.sendMessage(CC.translate(KitLocale.KIT_DISCLAIMER_SET.getMessage()).replace("{kit-name}", kitName).replace("{disclaimer}", disclaimer));
    }
}