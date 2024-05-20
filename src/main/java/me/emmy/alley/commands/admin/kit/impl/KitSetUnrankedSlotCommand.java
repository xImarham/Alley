package me.emmy.alley.commands.admin.kit.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 21/05/2024 - 00:22
 */

public class KitSetUnrankedSlotCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setunrankedslot", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&cUsage: /kit setunrankedslot (kit-name) (slot)"));
            return;
        }

        String kitName = args[0];
        int slot = Integer.parseInt(args[1]);

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            player.sendMessage(CC.translate(ConfigLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setUnrankedslot(slot);
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate(ConfigLocale.KIT_UNRANKEDSLOT_SET.getMessage()).replace("{kit-name}", kitName).replace("{editor-slot}", args[1]));
    }
}
