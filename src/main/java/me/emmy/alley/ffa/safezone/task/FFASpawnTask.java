package me.emmy.alley.ffa.safezone.task;

import me.emmy.alley.Alley;
import me.emmy.alley.ffa.enums.EnumFFAState;
import me.emmy.alley.ffa.safezone.cuboid.Cuboid;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Emmy
 * Project: Alley
 * Date: -
 */
public class FFASpawnTask extends BukkitRunnable {

    private final Cuboid cuboid;
    private final Alley plugin;
    private final Map<UUID, EnumFFAState> playerStates;

    public FFASpawnTask(Cuboid cuboid, Alley plugin) {
        this.cuboid = cuboid;
        this.plugin = plugin;
        this.playerStates = new HashMap<>();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());

            if (profile != null && profile.getState() == EnumProfileState.FFA) {
                EnumFFAState currentState = playerStates.getOrDefault(player.getUniqueId(), EnumFFAState.FIGHTING);
                boolean isInSpawn = cuboid.isIn(player);

                if (isInSpawn && currentState != EnumFFAState.SPAWN) {
                    profile.getFfaMatch().setState(EnumFFAState.SPAWN);
                    playerStates.put(player.getUniqueId(), EnumFFAState.SPAWN);
                    player.sendMessage(CC.translate("&aYou have entered the FFA spawn area."));
                } else if (!isInSpawn && currentState != EnumFFAState.FIGHTING) {
                    profile.getFfaMatch().setState(EnumFFAState.FIGHTING);
                    playerStates.put(player.getUniqueId(), EnumFFAState.FIGHTING);
                    player.sendMessage(CC.translate("&cYou have left the FFA spawn area."));
                }
            }
        }
    }
}