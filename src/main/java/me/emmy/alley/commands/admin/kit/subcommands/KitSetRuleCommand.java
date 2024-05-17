package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.KitType;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:27
 */

public class KitSetRuleCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setrule", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        if (args.length() < 2) {
            player.sendMessage(CC.translate("&cUsage: /kit setrule (kitName) (rule)"));
            return;
        }

        String kitName = args.getArgs()[0];
        String rule = args.getArgs()[1];

        Kit kit = Alley.getInstance().getKitManager().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate("&cThere is no kit with that name."));
            return;
        }

        try {
            KitType kitType = KitType.valueOf(rule.toUpperCase());
            List<String> types = new ArrayList<>(kit.getTypes());
            types.add(kitType.name());
            kit.setTypes(types);
            Alley.getInstance().getKitManager().saveKit(kitName, kit);
            player.sendMessage(CC.translate("&aSuccessfully added rule to kit!"));
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid rule name."));
        }
    }
}
