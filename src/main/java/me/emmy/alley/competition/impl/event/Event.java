package me.emmy.alley.competition.impl.event;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 22:07
 */
@Getter
@Setter
public class Event {
    private final String name;
    private String description;
    private int maxPlayers;
    private int teamSize;

    public Event() {
        EventTypeData data = getClass().getAnnotation(EventTypeData.class);
        this.name = data.name();
        this.description = data.description();
    }
}
