package dev.revere.alley.api.command;

import dev.revere.alley.Alley;

public abstract class BaseCommand {
    public final Alley plugin;

    /**
     * Constructor for the BaseCommand class.
     */
    public BaseCommand() {
        this.plugin = Alley.getInstance();
        this.plugin.getCommandFramework().registerCommands(this);
    }

    /**
     * Method to be called when a command is executed.
     *
     * @param command The command.
     */
    public abstract void onCommand(CommandArgs command);
}