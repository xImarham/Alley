package dev.revere.alley.game.party;

import dev.revere.alley.Alley;
import dev.revere.alley.game.party.enums.EnumPartyState;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 21:42
 */
@Getter
@Setter
public class Party {
    private Player leader;
    private List<UUID> members;
    private EnumPartyState state;

    /**
     * Constructor for the Party class.
     *
     * @param leader The leader of the party.
     */
    public Party(Player leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader.getUniqueId());
        this.state = EnumPartyState.PRIVATE;
        Alley.getInstance().getPartyHandler().getParties().add(this);
    }

    /**
     * Sens a message to all party members.
     *
     * @param message The message to notify the party members of.
     */
    public void notifyParty(String message) {
        for (UUID member : members) {
            Player player = Alley.getInstance().getServer().getPlayer(member);
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }
    }

    /**
     * Sends a message to all party members excluding the leader.
     *
     * @param message The message to notify the party members of.
     */
    public void notifyPartyExcludeLeader(String message) {
        for (UUID member : members) {
            if (member.equals(leader.getUniqueId())) {
                continue;
            }
            Player player = Alley.getInstance().getServer().getPlayer(member);
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }
    }
}