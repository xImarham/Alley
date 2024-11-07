package dev.revere.alley.kit.command.impl.data.slot;

import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 00:15
 */
public class KitSetEditorSlotCommand extends BaseCommand {
    @Command(name = "kit.seteditorslot", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit seteditorslot &b<kitName> <slot>"));
            return;
        }

        String kitName = args[0];
        int slot = Integer.parseInt(args[1]);

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setEditorslot(slot);
        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate(Locale.KIT_EDITORSLOT_SET.getMessage()).replace("{kit-name}", kitName).replace("{slot}", args[1]));
    }
}