package dev.revere.alley.kit.command.impl.storage;

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
 * @date 23/05/2024 - 01:11
 */
public class KitSaveCommand extends BaseCommand {
    @Command(name = "kit.save", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit save &b<kitName>"));
            return;
        }

        String kitName = args[0];

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        Alley.getInstance().getKitRepository().saveKit(kit);
        player.sendMessage(CC.translate(Locale.KIT_SAVED.getMessage().replace("{kit-name}", kitName)));
    }
}