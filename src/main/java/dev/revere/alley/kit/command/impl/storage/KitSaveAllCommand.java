package dev.revere.alley.kit.command.impl.storage;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:18
 */
public class KitSaveAllCommand extends BaseCommand {
    @Command(name = "kit.saveall", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Alley.getInstance().getKitRepository().saveKits();
        player.sendMessage(CC.translate(Locale.KIT_SAVED_ALL.getMessage()));
    }
}