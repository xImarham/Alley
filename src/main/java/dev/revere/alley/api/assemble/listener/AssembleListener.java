package dev.revere.alley.api.assemble.listener;

import dev.revere.alley.api.assemble.Assemble;
import dev.revere.alley.api.assemble.AssembleBoard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import dev.revere.alley.api.assemble.events.AssembleBoardCreateEvent;
import dev.revere.alley.api.assemble.events.AssembleBoardDestroyEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class AssembleListener implements Listener {
    private final Assemble assemble;

    /**
     * Constructor for the AssembleListener class.
     *
     * @param assemble The Assemble instance
     */
    public AssembleListener(Assemble assemble) {
        this.assemble = assemble;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.assemble.isCallEvents()) {
            AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);

            Bukkit.getPluginManager().callEvent(createEvent);
            if (createEvent.isCancelled()) {
                return;
            }
        }

        this.assemble.getBoards().put(player.getUniqueId(), new AssembleBoard(player, this.assemble));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (this.assemble.isCallEvents()) {
            AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(player);

            Bukkit.getPluginManager().callEvent(destroyEvent);
            if (destroyEvent.isCancelled()) {
                return;
            }
        }

        this.assemble.getBoards().remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}