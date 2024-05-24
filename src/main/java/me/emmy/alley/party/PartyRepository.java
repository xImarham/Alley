package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;

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

    public Party getPartyLeader(UUID uuid) {
        return parties.stream()
                .filter(party -> party.getLeader().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public Party getPartyMembers(UUID uuid) {
        return parties.stream()
                .filter(party -> party.getMembers().contains(uuid))
                .findFirst()
                .orElse(null);
    }

    public void createParty(UUID leader) {
        Party party = new Party(leader);
        parties.add(party);
    }

    public void disbandParty(UUID leader) {
        Party party = getPartyLeader(leader);
        if (party != null) {
            party.disband();
        }
    }

    public void leaveParty(UUID member) {
        Party party = getPartyMembers(member);
        if (party != null) {
            party.removeMember(member);
        }
    }

    public void kickMember(UUID leader, UUID member) {
        Party party = getPartyLeader(leader);
        if (party != null && party.getMembers().contains(member)) {
            party.removeMember(member);
        }
    }
}
