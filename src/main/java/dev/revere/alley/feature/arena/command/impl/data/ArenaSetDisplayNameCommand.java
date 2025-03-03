package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 11:45
 */
public class ArenaSetDisplayNameCommand extends BaseCommand {
    @CommandData(name = "arena.setdisplayname", permission = "alley.command.arena.setdisplayname", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/arena setdisplayname &b<arenaName> <displayName>"));
            return;
        }

        String arenaName = args[0];
        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            sender.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setDisplayName(displayName);
        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).saveArena();
        sender.sendMessage(CC.translate("&aSuccessfully set the display name of the arena &e" + arenaName + " &ato &e" + displayName + "&a."));
    }
}
