package dev.revere.alley.api.command;

import dev.revere.alley.Alley;

public abstract class BaseCommand {

    public Alley main = Alley.getInstance();

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
