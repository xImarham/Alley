package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.party.enums.EnumPartyState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 21/05/2024 - 21:42
 */

@Getter
@Setter
public class Party {
    private UUID leader;
    private boolean shared;
    private List<UUID> members;
    private EnumPartyState state;

    public Party(UUID leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.state = EnumPartyState.ACTIVE;
    }

    public void addMember(UUID member) {
        members.add(member);
    }

    public void removeMember(UUID member) {
        members.remove(member);
    }

    public void disband() {
        this.state = EnumPartyState.DISBANDED;
        this.members.clear();
    }
}
