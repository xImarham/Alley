package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.KitType;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:14
 */

public class KitCreateCommand extends BaseCommand {
    @Override
    @Command(name = "kit.create", permission = "practice.owner")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        if (args.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit create (kitName)"));
            return;
        }


        String kitName = args.getArgs()[0];

        Kit kit = Kit.builder()
                .displayName(kitName)
                .name(kitName)
                .description(kitName + " description")
                .items(player.getInventory().getContents())
                .armour(player.getInventory().getArmorContents())
                .types(Collections.singletonList(KitType.DAMAGE.name()))
                .build();

        Alley.getInstance().getKitManager().getKits().add(kit);
        Alley.getInstance().getKitManager().saveKit(kit.getName(), kit);
        player.sendMessage(CC.translate("&aSuccessfully created the kit called " + "&b" + kitName + "&a!"));
    }
}