package dev.revere.alley.game.match.snapshot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.revere.alley.Alley;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/07/2025
 */
public class SnapshotDataService {
    private final Map<UUID, Long> lastSprintStart;
    private final Map<UUID, Boolean> isSprinting;

    private final Alley plugin;

    /**
     * Constructor for the SnapshotDataService class.
     *
     * @param plugin The Alley instance
     */
    public SnapshotDataService(Alley plugin) {
        this.plugin = plugin;

        this.lastSprintStart = new HashMap<>();
        this.isSprinting = new HashMap<>();

        this.registerSprintListener();
    }

    private void registerSprintListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.ENTITY_ACTION) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerAction action = packet.getPlayerActions().read(0);

                UUID uuid = player.getUniqueId();

                switch (action) {
                    case START_SPRINTING:
                        lastSprintStart.put(uuid, System.currentTimeMillis());
                        isSprinting.put(uuid, true);
                        break;
                    case STOP_SPRINTING:
                    case START_SNEAKING:
                    case STOP_SNEAKING:
                        isSprinting.put(uuid, false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Checks if the player is in a W-Tap state.
     *
     * @param uuid The UUID of the player
     * @return true if the player is in a W-Tap state, false otherwise
     */
    public boolean isWTap(UUID uuid) {
        Long sprintStart = this.lastSprintStart.get(uuid);
        boolean sprinting = this.isSprinting.getOrDefault(uuid, false);

        return sprintStart != null && !sprinting && (System.currentTimeMillis() - sprintStart) <= 300;
    }

    /**
     * Resets the sprint state for a player.
     *
     * @param uuid The UUID of the player to reset sprint state for.
     */
    public void resetSprint(UUID uuid) {
        this.isSprinting.put(uuid, true);
    }

    /**
     * Removes the sprint state for a player.
     *
     * @param uuid The UUID of the player to remove sprint state for.
     */
    public void clearData(UUID uuid) {
        this.lastSprintStart.remove(uuid);
        this.isSprinting.remove(uuid);
    }
}