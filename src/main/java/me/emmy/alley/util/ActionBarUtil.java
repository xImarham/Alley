package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.Logger;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 07:26
 */
@UtilityClass
public class ActionBarUtil {
    /**
     * Method to send an action bar message to a player.
     *
     * @param player The player.
     * @param message The message.
     */
    private void sendMessage(Player player, String message, int durationSeconds) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

            if (durationSeconds > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        IChatBaseComponent clearChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}");
                        PacketPlayOutChat clearPacket = new PacketPlayOutChat(clearChatBaseComponent, (byte) 2);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(clearPacket);
                    }
                }.runTaskLater(Alley.getInstance(), durationSeconds * 20L);
            }
        } catch (Exception e) {
            Logger.debug("An error occurred while sending an action bar message to a player: " + e.getMessage());
        }
    }
}