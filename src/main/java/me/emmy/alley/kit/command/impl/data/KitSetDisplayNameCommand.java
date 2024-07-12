package me.emmy.alley.kit.command.impl.data;

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
 * @author Emmy
 * @project Practice
 * @date 28/04/2024 - 22:46
 */
public class KitSetDisplayNameCommand extends BaseCommand {
    @Override
    @Command(name = "kit.displayname", aliases = "kit.setdisplayname",permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&cUsage: /kit setdisplayname (kitName) (displayName)"));
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
