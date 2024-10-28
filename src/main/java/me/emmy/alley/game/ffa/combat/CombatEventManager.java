package me.emmy.alley.game.ffa.combat;

import lombok.Getter;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Infames
 * @date 23/07/2021 @ 20:08
 */
@Getter
public class CombatEventManager extends BukkitRunnable {
    private final Set<Player> combatSet = new HashSet<>();
    private final Map<Player, Integer> timeMap = new HashMap<>();

    private int count = 0;

    //Set combat to player and set time to 16

    /**
     * Set combat to player and set time to 16
     *
     * @param player the player
     * @param b the boolean
     */
    public void setCombatSet(Player player, boolean b) {
        if (b) {
            combatSet.add(player);
            timeMap.put(player, 16);
        } else {
            combatSet.remove(player);
            timeMap.remove(player);
        }
    }

    /**
     * Set combat time
     *
     * @param player the player
     * @param time the time
     */
    public void setCombatTime(Player player, int time) {
        timeMap.remove(player);
        timeMap.put(player, time);
    }

    /**
     * Check if a player is in combat or not
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isCombat(Player player) {
        return combatSet.contains(player);
    }

    /**
     * Get the combat time of a player
     *
     * @return the set
     */
    public int getCombatTime(Player player) {
        return timeMap.get(player);
    }

    @Override
    public void run() {
        count++;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.isCombat(player)) {
                int count = this.getCombatTime(player);
                --count;
                this.setCombatTime(player, count);
                if (count == 0) {
                    player.sendMessage(CC.translate("&aYou are no longer in combat"));
                    this.getCombatSet().remove(player);
                    this.getTimeMap().remove(player);
                }
            }

            if (count == 160) {
                count = 0;
            }
        }
    }
}