package me.emmy.alley.kit.command.impl.data;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSetIconCommand extends BaseCommand {
    @Command(name = "kit.seticon", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit seticon &b<kitName>"));
            return;
        }

        String kitName = args[0];

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        kit.setIcon(player.getItemInHand().getType());
        kit.setIconData(player.getItemInHand().getDurability());
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate("&aIcon has been set for kit &b" + kitName + "&a!"));
    }
}
