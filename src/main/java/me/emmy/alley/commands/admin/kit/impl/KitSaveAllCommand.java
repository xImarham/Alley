package me.emmy.alley.commands.admin.kit.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 23/05/2024 - 01:18
 */

public class KitSaveAllCommand extends BaseCommand {
    @Override
    @Command(name = "kit.saveall", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Alley.getInstance().getKitRepository().saveKits();
        player.sendMessage(CC.translate(ConfigLocale.KIT_SAVED_ALL.getMessage()));
    }
}
