package dev.revere.alley.queue.command.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.queue.command.admin.impl.QueueForceCommand;
import dev.revere.alley.queue.command.admin.impl.QueueReloadCommand;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 24/09/2024 - 20:51
 */
public class QueueCommand extends BaseCommand {

    public QueueCommand() {
        new QueueForceCommand();
        new QueueReloadCommand();
    }

    @Command(name = "queue", permission = "alley.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&b&lQueue Commands Help:"));
        sender.sendMessage(CC.translate(" &f● &b/queue force &8(&7player&8) &8(&7kit&8) &8<&7ranked&8> &7| Force a player into a queue"));
        //sender.sendMessage(CC.translate(" &f● &b/queue remove &8(&7player&8) &7| Remove a player from queue"));
        sender.sendMessage(CC.translate(" &f● &b/queue reload &7| Reload the queues"));
        sender.sendMessage("");
    }
}
