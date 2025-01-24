package dev.revere.alley.api.command;

import dev.revere.alley.Alley;

public abstract class BaseCommand {
    public Alley plugin;

    /**
     * Constructor for the BaseCommand class.
     */
    public BaseCommand() {
        this.plugin = Alley.getInstance();
        this.plugin.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);
}