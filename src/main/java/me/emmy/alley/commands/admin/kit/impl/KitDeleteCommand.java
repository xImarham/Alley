package me.emmy.alley.commands.admin.kit.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitDeleteCommand extends BaseCommand {
    @Override
    @Command(name = "kit.delete", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit delete (kit-name)"));
            return;
        }

        String kitName = args[0];
        Kit kit = Alley.getInstance().getKitManager().getKitByName(kitName);

        if (kit == null) {
            player.sendMessage(CC.translate(ConfigLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        Alley.getInstance().getKitManager().deleteKit(kit);
        player.sendMessage(CC.translate(ConfigLocale.KIT_DELETED.getMessage().replace("{kit-name}", kitName)));
    }
}
