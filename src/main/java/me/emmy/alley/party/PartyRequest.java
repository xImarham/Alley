package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:44
 */
@Getter
@Setter
public class PartyRequest {
    private final Player sender;
    private final Player target;

    /**
     * Constructor for the PartyRequest class.
     *
     * @param sender The player sending the request.
     * @param target The player receiving the request.
     */
    public PartyRequest(Player sender, Player target) {
        this.sender = sender;
        this.target = target;
    }
}