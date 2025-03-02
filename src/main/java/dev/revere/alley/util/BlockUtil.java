package dev.revere.alley.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/03/2025
 */
@UtilityClass
public class BlockUtil {
    /**
     * Sends a block break animation to all players.
     *
     * @param block       The block to send the animation for.
     * @param animationId The ID of the animation.
     * @param stage       The stage of the animation.
     */
    public void processBreakAnimation(Block block, int animationId, int stage) {
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                animationId,
                new BlockPosition(block.getX(), block.getY(), block.getZ()),
                stage
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}