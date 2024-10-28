package me.emmy.alley.essential.command.troll;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.chat.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 09:00
 */
public class TrollCommand extends BaseCommand {
    @Override
    @Command(name = "troll", aliases = "playertroll", inGameOnly = false, permission = "alley.command.troll")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/troll &b(player)"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        try {
            String path = Alley.getInstance().getServer().getClass().getPackage().getName();
            String version = path.substring(path.lastIndexOf(".") + 1);

            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Class<?> PacketPlayOutGameStateChange = Class.forName("net.minecraft.server." + version + ".PacketPlayOutGameStateChange");
            Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
            Constructor<?> playOutConstructor = PacketPlayOutGameStateChange.getConstructor(int.class, float.class);
            Object packet = playOutConstructor.newInstance(5, 0);
            Object craftPlayerObject = craftPlayer.cast(targetPlayer);
            Method getHandleMethod = craftPlayer.getMethod("getHandle");
            Object handle = getHandleMethod.invoke(craftPlayerObject);
            Object pc = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);
            sendPacketMethod.invoke(pc, packet);
        } catch (Exception ex) {
            Logger.debug("An error occurred while trying to troll " + targetPlayer.getName() + ": " + ex.getMessage());
        }

        sender.sendMessage(CC.translate("&eYou have trolled &d" + targetPlayer.getName() + "&e."));
        targetPlayer.sendMessage(CC.translate("&eYou have been trolled."));
    }
}