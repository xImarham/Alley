package me.emmy.alley.game.duel;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:04
 */
@Getter
@Setter
public class DuelRequest {
    private final Player sender;
    private final Player target;
    private Kit kit;
    private Arena arena;

    private final long expireTime;

    /**
     * Instantiates a new Duel request.
     *
     * @param sender the sender
     * @param target the target
     * @param kit    the kit
     * @param arena  the arena
     */
    public DuelRequest(Player sender, Player target, Kit kit, Arena arena) {
        this.sender = sender;
        this.target = target;
        this.kit = kit;
        this.arena = arena;
        this.expireTime = System.currentTimeMillis() + 30000L;
    }

    /**
     * Check if the duel request has expired.
     *
     * @return true if the duel request has expired, false otherwise
     */
    public boolean hasExpired() {
        return System.currentTimeMillis() > expireTime;
    }

    public long getRemainingTime() {
        return expireTime - System.currentTimeMillis();
    }

    public String getRemainingTimeFormatted() {
        long seconds = getRemainingTime() / 1000;
        long minutes = seconds / 60;
        return String.format("%02d:%02d", minutes, seconds % 60);
    }
}