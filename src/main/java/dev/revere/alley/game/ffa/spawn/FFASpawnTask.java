package dev.revere.alley.game.ffa.spawn;

import dev.revere.alley.Alley;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.ffa.enums.EnumFFAState;
import dev.revere.alley.util.data.cuboid.Cuboid;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 22:26
 */
public class FFASpawnTask extends BukkitRunnable {
    private final Cuboid cuboid;
    private final Alley plugin;
    private final Map<UUID, EnumFFAState> playerStates;

    /**
     * Constructor for the FFASpawnTask class.
     *
     * @param cuboid The cuboid of the safezone.
     * @param plugin The instance of the plugin.
     */
    public FFASpawnTask(Cuboid cuboid, Alley plugin) {
        this.cuboid = cuboid;
        this.plugin = plugin;
        this.playerStates = new HashMap<>();
    }

    @Override
    public void run() {
        for (AbstractFFAMatch abstractFFAMatch : plugin.getFfaRepository().getMatches()) {
            if (abstractFFAMatch.getPlayers().isEmpty()) {
                continue;
            }

            for (Player player : abstractFFAMatch.getPlayers()) {
                EnumFFAState currentState = playerStates.getOrDefault(player.getUniqueId(), EnumFFAState.FIGHTING);
                boolean isInSpawn = cuboid.isIn(player);

                Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
                if (profile != null && profile.getState() == EnumProfileState.FFA) {
                    if (isInSpawn && currentState != EnumFFAState.SPAWN) {
                        abstractFFAMatch.setState(EnumFFAState.SPAWN);
                        playerStates.put(player.getUniqueId(), EnumFFAState.SPAWN);
                        player.sendMessage(CC.translate("&aYou have entered the FFA spawn area."));
                    } else if (!isInSpawn && currentState != EnumFFAState.FIGHTING) {
                        abstractFFAMatch.setState(EnumFFAState.FIGHTING);
                        playerStates.put(player.getUniqueId(), EnumFFAState.FIGHTING);
                        player.sendMessage(CC.translate("&cYou have left the FFA spawn area."));
                    }
                }
            }
        }
    }
}