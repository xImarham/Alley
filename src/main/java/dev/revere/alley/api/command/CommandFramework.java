package dev.revere.alley.api.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class CommandFramework implements CommandExecutor {

    private final Map<String, Entry<Method, Object>> commandMap;
    private final Alley plugin;
    private CommandMap map;

    /**
     * Constructor for the CommandFramework class.
     *
     * @param plugin the plugin
     */
    public CommandFramework(Alley plugin) {
        this.commandMap = new HashMap<>();
        this.plugin = plugin;
        this.initializeMap(plugin);
    }

    /**
     * Initializes the command map for the plugin manager.
     *
     * @param plugin the plugin
     */
    private void initializeMap(Alley plugin) {
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                this.map = (CommandMap) field.get(manager);
            } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
                Bukkit.getConsoleSender().sendMessage("Failed to register commands: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    @SuppressWarnings("all")
    public boolean handleCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (this.commandMap.containsKey(cmdLabel)) {
                Method method = this.commandMap.get(cmdLabel).getKey();
                Object methodObject = this.commandMap.get(cmdLabel).getValue();
                CommandData commandData = method.getAnnotation(CommandData.class);

                PluginConstant pluginConstant = this.plugin.getPluginConstant();
                String noPermission = pluginConstant.getPermissionLackMessage();

                if (commandData.isAdminOnly() && !sender.hasPermission(pluginConstant.getAdminPermissionPrefix())) {
                    sender.sendMessage(noPermission);
                    return true;
                }
                if (!commandData.permission().isEmpty() && (!sender.hasPermission(commandData.permission()))) {
                    sender.sendMessage(CC.translate(noPermission));
                    return true;
                }
                if (commandData.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command is only performable in game.");
                    return true;
                }

                try {
                    method.invoke(methodObject,
                            new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    Bukkit.getConsoleSender().sendMessage("Failed to execute command: " + cmdLabel + " - " + e.getMessage());
                    e.printStackTrace();
                }
                return true;
            }
        }
        this.defaultCommand(new CommandArgs(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(CommandData.class) != null) {
                CommandData commandData = m.getAnnotation(CommandData.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                registerCommand(commandData, Objects.requireNonNull(commandData).name(), m, obj);
                for (String alias : commandData.aliases()) {
                    registerCommand(commandData, alias, m, obj);
                }
            } else if (m.getAnnotation(CompleterData.class) != null) {
                CompleterData comp = m.getAnnotation(CompleterData.class);
                if (m.getParameterTypes().length != 1
                        || m.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println(
                            "Unable to register tab completer " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                if (m.getReturnType() != List.class) {
                    System.out.println("Unable to register tab completer " + m.getName() + ". Unexpected return type");
                    continue;
                }
                registerCompleter(Objects.requireNonNull(comp).name(), m, obj);
                for (String alias : comp.aliases()) {
                    registerCompleter(alias, m, obj);
                }
            }
        }
    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : this.commandMap.keySet()) {
            if (!s.contains(".")) {
                Command cmd = map.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getPluginConstant().getName(), "All commands for " + plugin.getPluginConstant().getName(), null, help,
                "Below is a list of all " + plugin.getPluginConstant().getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    public void unregisterCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(CommandData.class) != null) {
                CommandData commandData = m.getAnnotation(CommandData.class);
                this.commandMap.remove(Objects.requireNonNull(commandData).name().toLowerCase());
                this.commandMap.remove(this.plugin.getPluginConstant().getName() + ":" + commandData.name().toLowerCase());
                this.map.getCommand(commandData.name().toLowerCase()).unregister(map);
            }
        }
    }

    public void registerCommand(CommandData commandData, String label, Method m, Object obj) {
        this.commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        this.commandMap.put(this.plugin.getPluginConstant().getName() + ':' + label.toLowerCase(),
                new AbstractMap.SimpleEntry<>(m, obj));
        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getPluginConstant().getName(), cmd);
        }
        if (!commandData.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setDescription(commandData.description());
        }
        if (!commandData.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setUsage(commandData.usage());
        }
    }

    public void registerCompleter(String label, Method m, Object obj) {
        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
        if (map.getCommand(cmdLabel) == null) {
            Command command = new BukkitCommand(cmdLabel, this, plugin);
            map.register(plugin.getPluginConstant().getName(), command);
        }
        if (map.getCommand(cmdLabel) instanceof BukkitCommand) {
            BukkitCommand command = (BukkitCommand) map.getCommand(cmdLabel);
            if (command.completer == null) {
                command.completer = new BukkitCompleter();
            }
            command.completer.addCompleter(label, m, obj);
        } else if (map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                Object command = map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);
                if (field.get(command) == null) {
                    BukkitCompleter completer = new BukkitCompleter();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                } else if (field.get(command) instanceof BukkitCompleter) {
                    BukkitCompleter completer = (BukkitCompleter) field.get(command);
                    completer.addCompleter(label, m, obj);
                } else {
                    System.out.println("Unable to register tab completer " + m.getName()
                            + ". A tab completer is already registered for that command!");
                }
            } catch (Exception exception) {
                System.out.println("Failed to register tab completer " + m.getName() + " for command " + label + ": " + exception.getMessage());
            }
        }
    }

    private void defaultCommand(CommandArgs args) {

        String label = args.getLabel();
        String[] parts = label.split(":");

        if (args.getSender().hasPermission(Alley.getInstance().getConfigService().getSettingsConfig().getString("command.syntax-bypass-perm"))) {
            if (parts.length > 1) {
                String commandToExecute = parts[1];

                StringBuilder commandBuilder = new StringBuilder(commandToExecute);
                for (String arg : args.getArgs()) {
                    commandBuilder.append(" ").append(arg);
                }
                String command = commandBuilder.toString();

                if (args.getSender() instanceof Player) {
                    ((Player) args.getSender()).performCommand(command);
                } else {
                    args.getSender().getServer().dispatchCommand(args.getSender(), command);
                }
            } else {
                args.getSender().sendMessage(CC.translate("&cMissing arguments / Wrong format or Internal error."));
            }
        } else {
            args.getSender().sendMessage(CC.translate(Alley.getInstance().getConfigService().getSettingsConfig().getString("command.anti-syntax-message").replace("{argument}", args.getLabel())));
        }
    }
}