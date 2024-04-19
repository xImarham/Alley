package me.emmy.pluginbase.utils.command;

import me.emmy.pluginbase.PluginBase;

public abstract class BaseCommand {

    public PluginBase main = PluginBase.getInstance();

    public BaseCommand() {
        main.getFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
