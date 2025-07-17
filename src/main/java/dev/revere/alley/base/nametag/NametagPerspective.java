package dev.revere.alley.base.nametag;

import dev.revere.alley.base.nametag.strategy.NametagStrategy;
import dev.revere.alley.base.nametag.strategy.impl.DefaultStrategy;
import dev.revere.alley.base.nametag.strategy.impl.MatchStrategy;
import dev.revere.alley.base.nametag.strategy.impl.SpectatorStrategy;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagPerspective {
    private final Map<UUID, NametagAdapter> displayedAdapters = new ConcurrentHashMap<>();
    private final Set<String> knownAdapters;

    private final List<NametagStrategy> strategies;
    private final NametagService service;
    private final Player viewer;

    public NametagPerspective(NametagService service, Player viewer, NametagRegistry registry) {
        this.service = service;
        this.viewer = viewer;

        this.knownAdapters = registry.getAdapterCache().asMap().values().stream()
                .map(NametagAdapter::getName)
                .collect(Collectors.toSet());

        this.strategies = new CopyOnWriteArrayList<>();
        this.strategies.add(new SpectatorStrategy());
        this.strategies.add(new MatchStrategy());
        this.strategies.add(new DefaultStrategy());
    }

    /**
     * Calculates and applies the nametag for a target player, as seen by this manager's owner.
     *
     * @param target The player whose nametag is to be updated for the viewer.
     */
    public void updateNametagFor(Player target) {
        if (!viewer.isOnline() || !target.isOnline()) {
            return;
        }

        NametagView newView = determineView(new NametagContext(viewer, target));
        if (newView == null) return;

        NametagAdapter previousAdapter = displayedAdapters.get(target.getUniqueId());
        if (previousAdapter != null && previousAdapter.represents(newView)) {
            return;
        }

        NametagAdapter newAdapter = service.getNametagRegistry().getAdapter(newView);

        if (previousAdapter != null) {
            previousAdapter.removePlayer(target, this.viewer);
        }

        if (!knownAdapters.contains(newAdapter.getName())) {
            newAdapter.sendCreationPacket(viewer);
            knownAdapters.add(newAdapter.getName());
        }

        newAdapter.addPlayer(target, this.viewer);

        displayedAdapters.put(target.getUniqueId(), newAdapter);
    }

    private NametagView determineView(NametagContext context) {
        for (NametagStrategy strategy : strategies) {
            NametagView view = strategy.createNametagView(context);
            if (view != null) {
                return view;
            }
        }
        return null;
    }
}
