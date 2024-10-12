package me.emmy.alley.game.event;

import lombok.Getter;
import me.emmy.alley.game.event.impl.SumoEventImpl;
import me.emmy.alley.util.chat.Logger;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 12/10/2024 - 22:32
 */
@Getter
public class EventRepository {
    private final List<Event> events = new ArrayList<>();

    public EventRepository() {
        loadEvents();
    }

    /**
     * Load all events
     */
    public void loadEvents() {
        events.add(new SumoEventImpl());
    }

    /**
     * Get an event by name
     *
     * @param name The name of the event
     * @return The event
     */
    public Event getEvent(String name) {
        return events
                .stream()
                .filter(event -> event.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get an event by class
     *
     * @param clazz The class of the event
     * @return The event
     */
    public Event getByClass(Class<? extends Event> clazz) {
        return events
                .stream()
                .filter(event -> event.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }
}