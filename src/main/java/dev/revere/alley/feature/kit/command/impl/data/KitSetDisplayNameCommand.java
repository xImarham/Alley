package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.impl.KitLocale;
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
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setDisplayName(displayName);
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_DISPLAYNAME_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{display-name}", displayName));
    }
}