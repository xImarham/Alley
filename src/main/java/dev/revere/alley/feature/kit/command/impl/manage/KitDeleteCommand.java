package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.ActionBarUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024
 */
public class KitDeleteCommand extends BaseCommand {
    @CommandData(name = "kit.delete", permission = "alley.admin")
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
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        Alley.getInstance().getKitRepository().deleteKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_DELETED.getMessage().replace("{kit-name}", kitName)));
        ActionBarUtil.sendMessage(player, KitLocale.KIT_DELETED.getMessage().replace("{kit-name}", kitName), 5);

        Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> {
            if (arena.getKits().contains(kitName)) {
                arena.getKits().remove(kitName);
                arena.saveArena();
            }
        });
        player.sendMessage(CC.translate("&7Do not forget to reload the queues by using &c&l/queue reload&7."));
        player.sendMessage(CC.translate("&7Additionally, the kit has been removed from all arenas it was added to."));
    }
}