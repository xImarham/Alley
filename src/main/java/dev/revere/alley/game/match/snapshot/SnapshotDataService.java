package dev.revere.alley.game.match.snapshot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.revere.alley.Alley;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/07/2025
 */
@Service(provides = ISnapshotDataService.class, priority = 400)
public class SnapshotDataService implements ISnapshotDataService {
    private final Alley plugin;

    private final Map<UUID, Long> lastSprintStart = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> isSprinting = new ConcurrentHashMap<>();
    private PacketAdapter packetAdapter;

    /**
     * Constructor for DI.
     */
    public SnapshotDataService(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            Logger.warn("ProtocolLib not found, W-Tap detection will be disabled.");
            return;
        }
        this.registerSprintListener();
    }

    @Override
    public void shutdown(AlleyContext context) {
        if (this.packetAdapter != null) {
            ProtocolLibrary.getProtocolManager().removePacketListener(this.packetAdapter);
        }
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

    @Override
    public boolean isWTap(UUID uuid) {
        Long sprintStart = this.lastSprintStart.get(uuid);
        boolean sprinting = this.isSprinting.getOrDefault(uuid, false);

        return sprintStart != null && !sprinting && (System.currentTimeMillis() - sprintStart) <= 300;
    }

    @Override
    public void resetSprint(UUID uuid) {
        this.isSprinting.put(uuid, true);
    }

    @Override
    public void clearData(UUID uuid) {
        this.lastSprintStart.remove(uuid);
        this.isSprinting.remove(uuid);
    }
}