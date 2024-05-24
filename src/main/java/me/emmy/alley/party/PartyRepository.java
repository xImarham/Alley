package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 21/05/2024 - 21:45
 */

@Getter
@Setter
public class PartyRepository {
    private final List<Party> parties = new ArrayList<>();

    /**
     * Gets the party of a leader.
     *
     * @param player The leader of the party.
     * @return The party of the leader.
     */
    public Party getPartyByLeader(Player player) {
        return parties.stream()
                .filter(party -> party.getLeader().equals(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the party of a member.
     *
     * @param uuid The member's UUID.
     * @return The party of the member.
     */
    public Party getPartyByMember(UUID uuid) {
        return parties.stream()
                .filter(party -> party.getMembers().contains(uuid))
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a party.
     *
     * @param leader The leader of the party.
     */
    public void createParty(Player leader) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(leader.getUniqueId());
        Party party = new Party(leader);
        profile.setParty(party);

        if (profile.getState().equals(EnumProfileState.LOBBY)) {
            Alley.getInstance().getHotbarUtility().applyPartyItems(leader);
        }
    }

    /**
     * Disbands the party.
     *
     * @param leader The leader of the party.
     */
    public void disbandParty(Player leader) {
        Party party = getPartyByLeader(leader);
        if (party != null) {
            party.disband();
        }
    }

    /**
     * Joins a party.
     *
     * @param player The player to join the party.
     * @param leader The leader of the party.
     */
    public void joinParty(Player player, Player leader) {
        Party party = getPartyByLeader(leader);
        if (party != null) {
            party.joinParty(player);
        }
    }

    /**
     * Removes a member from the party.
     *
     * @param player The member to remove.
     */
    public void leaveParty(Player player) {
        Party party = getPartyByMember(player.getUniqueId());
        if (party != null) {
            party.leaveParty(player);
        }
    }

    /**
     * Kicks a member from the party.
     *
     * @param leader The leader of the party.
     * @param member The member to kick.
     */
    public void kickMember(Player leader, Player member) {
        Party party = getPartyByLeader(leader);
        if (party != null && party.getMembers().contains(member.getUniqueId())) {
            party.leaveParty(member);
        }
    }
}
