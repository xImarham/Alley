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

    /**
     * Instantiates a new Duel request.
     *
     * @param sender the sender
     * @param target the target
     */
    public DuelRequest(Player sender, Player target, Kit kit, Arena arena) {
        this.sender = sender;
        this.target = target;
        this.kit = kit;
        this.arena = arena;
    }
}
