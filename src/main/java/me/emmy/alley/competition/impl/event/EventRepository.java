package me.emmy.alley.competition.impl.event;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.competition.impl.event.impl.EventTypeSumoImpl;
import me.emmy.alley.competition.impl.event.impl.EventTypeTntTagImpl;
import me.emmy.alley.utils.chat.CC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:12
 */
@Getter
@Setter
public class EventRepository {
    private final List<Event> events = new ArrayList<>();
    private final Map<String, Class<? extends Event>> eventClasses = new HashMap<>();

    public EventRepository() {
        this.registerEventType(EventTypeSumoImpl.class);
        this.registerEventType(EventTypeTntTagImpl.class);
    }

    public void registerEventType(Class<? extends Event> clazz) {
        try {
            Event instance = clazz.getDeclaredConstructor().newInstance();
            events.add(instance);
            eventClasses.put(instance.getName(), clazz);
        } catch (Exception e) {
            CC.sendError("Failed to register event type class " + clazz.getSimpleName() + "!");
        }
    }
}
