package dev.revere.alley.tool.reflection.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.IReflection;
import dev.revere.alley.util.chat.CC;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class ActionBarReflectionService implements IReflection {
    protected final Alley plugin;

    /**
     * Constructor for the ActionBarReflectionService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ActionBarReflectionService(Alley plugin) {
        this.plugin = plugin;
    }

    /**
     * Method to send an action bar message to a player in a specific interval.
     *
     * @param player          The player.
     * @param message         The message.
     * @param durationSeconds The duration to show the message (in seconds).
     */
    public void sendMessage(Player player, String message, int durationSeconds) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            this.sendPacket(player, packet);

            if (durationSeconds > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        IChatBaseComponent clearChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}");
                        PacketPlayOutChat clearPacket = new PacketPlayOutChat(clearChatBaseComponent, (byte) 2);
                        sendPacket(player, clearPacket);
                    }
                }.runTaskLater(this.plugin, durationSeconds * 20L);
            }
        } catch (Exception exception) {
            Logger.logException("An error occurred while trying to send an action bar message to " + player.getName(), exception);
        }
    }

    /**
     * Method to send an action bar message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    public void sendMessage(Player player, String message) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            this.sendPacket(player, packet);
        } catch (Exception exception) {
            Logger.logException("An error occurred while trying to send an action bar message to " + player.getName(), exception);
        }
    }

    /**
     * Sends a death message to the killer.
     *
     * @param killer The player who killed the victim.
     * @param victim The player who died.
     */
    public void sendDeathMessage(Player killer, Player victim) {
        this.sendMessage(killer, "&c&lKILL! &f" + victim.getName(), 3);
    }
}