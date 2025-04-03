package dev.revere.alley.reflection.impl;

import dev.revere.alley.reflection.IReflection;
import dev.revere.alley.util.chat.CC;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class TitleReflectionService implements IReflection {
    /**
     * Send a title to a player with a subtitle and fade in/out times.
     *
     * @param player   the player to send the title to
     * @param title    the title to send
     * @param subtitle the subtitle to send
     * @param fadeIn   the fade in time
     * @param stay     the stay time
     * @param fadeOut  the fade out time
     */
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        CraftPlayer craftPlayer = this.getCraftPlayer(player);
        if (craftPlayer == null) {
            return;
        }

        player.sendMessage(CC.translate(title));

        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + CC.translate(title) + "\"}");
        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + CC.translate(subtitle) + "\"}");

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent, fadeIn, stay, fadeOut);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent, fadeIn, stay, fadeOut);

        this.sendPacket(player, titlePacket);
        this.sendPacket(player, subtitlePacket);
    }
}