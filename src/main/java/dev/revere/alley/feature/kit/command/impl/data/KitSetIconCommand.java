package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSetIconCommand extends BaseCommand {
    @CommandData(name = "kit.seticon", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit seticon &b<kitName>"));
            return;
        }

        String kitName = args[0];

        Kit kit = Alley.getInstance().getKitService().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setIcon(player.getItemInHand().getType());
        kit.setIconData(player.getItemInHand().getDurability());
        Alley.getInstance().getKitService().saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_ICON_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{icon}", player.getItemInHand().getType().name().toUpperCase() + ":" + player.getItemInHand().getDurability()));
    }
}
