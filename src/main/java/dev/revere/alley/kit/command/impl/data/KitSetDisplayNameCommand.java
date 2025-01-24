package dev.revere.alley.kit.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:46
 */
public class KitSetDisplayNameCommand extends BaseCommand {
    @Command(name = "kit.displayname", aliases = "kit.setdisplayname",permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit displayname &b<kitName> <displayName>"));
            return;
        }

        String kitName = args[0];
        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setDisplayName(displayName);
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate("&aSuccessfully set the display name &b" + displayName + " &afor the kit &b" + kit.getName() + "&a."));
    }
}