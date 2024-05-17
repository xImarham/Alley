package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.KitType;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:34
 */

public class KitViewRulesCommand extends BaseCommand {
    @Override
    @Command(name = "kit.viewrules", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        if (args.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit viewrules (kitName)"));
            return;
        }

        String kitName = args.getArgs()[0];

        Kit kit = Alley.getInstance().getKitManager().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate("&cThere is no kit with that name."));
            return;
        }

        player.sendMessage(" ");
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("&d&lKit Commands Help:"));
        for (KitType type : KitType.values()) {
            player.sendMessage(CC.translate("&7- &d" + type.name() + " &8- &7" + (kit.getTypes().contains(type.name()) ? "&atrue" : "&cfalse")));
        }
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(" ");
    }
}
