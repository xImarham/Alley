package dev.revere.alley.base.arena.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.arena.impl.FreeForAllArena;
import dev.revere.alley.base.arena.impl.SharedArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.arena.selection.ArenaSelection;
import dev.revere.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaCreateCommand extends BaseCommand {

    @CommandData(name = "arena.create", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena create &6<arenaName> <type>"));
            return;
        }

        String arenaName = args[0];
        EnumArenaType arenaType = Arrays.stream(EnumArenaType.values())
                .filter(type -> type.name().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);

        if (arenaType == null) {
            player.sendMessage(CC.translate("&cInvalid arena type! Valid types: SHARED, STANDALONE, FFA"));
            return;
        }

        if (this.plugin.getArenaService().getArenaByName(arenaName) != null) {
            player.sendMessage(CC.translate("&cAn arena with that name already exists!"));
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(CC.translate("&cYou must select the minimum and maximum locations for the arena."));
            return;
        }

        AbstractArena arena;
        switch (arenaType) {
            case SHARED:
                arena = new SharedArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum());
                break;
            case STANDALONE:
                arena = new StandAloneArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum(), null, null, 7, 70);
                break;
            case FFA:
                arena = new FreeForAllArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum());
                break;
            default:
                return;
        }

        arena.setDisplayName(Objects.requireNonNull(getDefaultDisplayName(arenaType)).replace("{arena-name}", arenaName));

        arena.createArena();
        player.sendMessage(CC.translate("&aSuccessfully created a new arena named &6" + arenaName + "&a with type &6" + arenaType.name() + "&a!"));
    }

    /**
     * Get the default display name for the specified arena type.
     *
     * @param arenaType The type of the arena.
     * @return The default display name.
     */
    private String getDefaultDisplayName(EnumArenaType arenaType) {
        FileConfiguration config = this.plugin.getConfigService().getSettingsConfig();

        switch (arenaType) {
            case SHARED:
                return config.getString("arena.default-display-name.shared");
            case STANDALONE:
                return config.getString("arena.default-display-name.standalone");
            case FFA:
                return config.getString("arena.default-display-name.ffa");
        }

        return null;
    }
}
