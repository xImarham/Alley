package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;

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
    public List<UUID> members;

    public Party(UUID leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
    }

}
