package me.emmy.alley.competition.impl.event;

import me.emmy.alley.kit.Kit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventTypeData {
    String name();
    String description();
}