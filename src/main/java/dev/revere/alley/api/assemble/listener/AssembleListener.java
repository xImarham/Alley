package dev.revere.alley.api.assemble.listener;

import dev.revere.alley.api.assemble.Assemble;
import dev.revere.alley.api.assemble.AssembleBoard;
import dev.revere.alley.api.assemble.IAssembleService;
import dev.revere.alley.api.assemble.events.AssembleBoardCreateEvent;
import dev.revere.alley.api.assemble.events.AssembleBoardDestroyEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class AssembleListener implements Listener {
    private final IAssembleService assembleService;

    public AssembleListener(IAssembleService assembleService) {
        this.assembleService = assembleService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.assembleService.createBoard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.assembleService.removeBoard(player);
    }
}