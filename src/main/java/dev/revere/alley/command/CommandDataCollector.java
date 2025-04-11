package dev.revere.alley.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class CommandDataCollector {
    /**
     * Collects command names from classes annotated with @CommandData in the specified package.
     *
     * @param basePackage The base package to scan for command classes.
     * @return A set of command names.
     */
    public Set<String> collectCommandNames(String basePackage) {
        Set<String> commandNames = new HashSet<>();

        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(CommandData.class);

        for (Class<?> clazz : commandClasses) {
            if (!BaseCommand.class.isAssignableFrom(clazz)) continue;

            CommandData commandData = clazz.getAnnotation(CommandData.class);
            if (commandData != null) {
                String name = commandData.name();
                commandNames.add(name.toLowerCase());
            }
        }

        return commandNames;
    }
}