package me.emmy.alley.kit.command.impl.data.slot;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 00:15
 */
public class KitSetEditorSlotCommand extends BaseCommand {
    @Override
    @Command(name = "kit.seteditorslot", permission = "alley.admin")
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
