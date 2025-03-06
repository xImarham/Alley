package dev.revere.alley.api.assemble;

import dev.revere.alley.Alley;
import dev.revere.alley.api.assemble.enums.EnumAssembleStyle;
import dev.revere.alley.api.assemble.events.AssembleBoardCreateEvent;
import dev.revere.alley.api.assemble.interfaces.IAssembleAdapter;
import dev.revere.alley.api.assemble.listener.AssembleListener;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Assemble {
    private final ChatColor[] chatColorCache = ChatColor.values();

    private Map<UUID, AssembleBoard> boards;

    private IAssembleAdapter adapter;
    private AssembleThread thread;
    private AssembleListener listeners;
    private EnumAssembleStyle assembleStyle;

    private final Alley plugin;

    private boolean hook;
    private boolean debugMode;
    private boolean callEvents;

    private long ticks;


    /**
     * Constructor for the Assemble class.
     *
     * @param alley   The plugin instance of Alley.
     * @param adapter The adapter instance.
     */
    public Assemble(Alley alley, IAssembleAdapter adapter) {
        if (alley == null) {
            throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
        }

        this.plugin = alley;
        this.adapter = adapter;
        this.assembleStyle = EnumAssembleStyle.MODERN;
        this.boards = new ConcurrentHashMap<>();
        this.ticks = 2;

        this.hook = false;
        this.debugMode = true;
        this.callEvents = true;

        this.setup();
    }

    public void setup() {
        this.listeners = new AssembleListener(this);
        this.plugin.getServer().getPluginManager().registerEvents(this.listeners, this.plugin);

        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }

        for (Player player : this.getPlugin().getServer().getOnlinePlayers()) {

            if (this.isCallEvents()) {
                AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);

                Bukkit.getPluginManager().callEvent(createEvent);
                if (createEvent.isCancelled()) {
                    continue;
                }
            }

            this.boards.putIfAbsent(player.getUniqueId(), new AssembleBoard(player, this));
        }

        this.thread = new AssembleThread(this);
    }

    /**
     * Interrupts the Assemble thread and removes all boards from players.
     *
     * @param onDisable whether the server is stopping.
     */
    public void interruptAndClose(boolean onDisable) {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }

        if (this.listeners != null) {
            HandlerList.unregisterAll(this.listeners);
            this.listeners = null;
        }

        for (UUID uuid : this.boards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            this.boards.remove(uuid);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        if (onDisable) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.ERROR_PREFIX + "&cAssemble has been interrupted and closed due to server stop."));
        }
    }
}