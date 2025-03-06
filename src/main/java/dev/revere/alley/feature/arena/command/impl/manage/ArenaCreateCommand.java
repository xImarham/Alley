package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.feature.arena.impl.FreeForAllArena;
import dev.revere.alley.feature.arena.impl.SharedArena;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.feature.arena.selection.ArenaSelection;
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

    @CommandData(name = "arena.create", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena create &b<arenaName> <type>"));
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

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) != null) {
            player.sendMessage(CC.translate("&cAn arena with that name already exists!"));
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(CC.translate("&cYou must select the minimum and maximum locations for the arena."));
            return;
        }

        Arena arena;
        switch (arenaType) {
            case SHARED:
                arena = new SharedArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum());
                break;
            case STANDALONE:
                arena = new StandAloneArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum(), null, null, 100);
                break;
            case FFA:
                arena = new FreeForAllArena(arenaName, arenaSelection.getMinimum(), arenaSelection.getMaximum());
                break;
            default:
                return;
        }

        arena.setDisplayName(Objects.requireNonNull(getDefaultDisplayName(arenaType)).replace("{arena-name}", arenaName));

        arena.createArena();
        player.sendMessage(CC.translate("&aSuccessfully created a new arena named &b" + arenaName + "&a with type &b" + arenaType.name() + "&a!"));
    }

    /**
     * Get the default display name for the specified arena type.
     *
     * @param arenaType The type of the arena.
     * @return The default display name.
     */
    private String getDefaultDisplayName(EnumArenaType arenaType) {
        FileConfiguration config = Alley.getInstance().getConfigService().getSettingsConfig();

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
