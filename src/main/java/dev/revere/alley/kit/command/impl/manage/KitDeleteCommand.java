package dev.revere.alley.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.util.ActionBarUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024
 */
public class KitDeleteCommand extends BaseCommand {
    @Command(name = "kit.delete", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit delete &b<kitName>"));
            return;
        }

        String kitName = args[0];
        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        Alley.getInstance().getKitRepository().deleteKit(kit);
        player.sendMessage(CC.translate(Locale.KIT_DELETED.getMessage().replace("{kit-name}", kitName)));
        ActionBarUtil.sendMessage(player, Locale.KIT_DELETED.getMessage().replace("{kit-name}", kitName), 5);

        Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> {
            if (arena.getKits().contains(kitName)) {
                arena.getKits().remove(kitName);
                arena.saveArena();
            }
        });
        player.sendMessage(CC.translate("&7Additionally, the kit has been removed from all arenas it was added to."));
    }
}