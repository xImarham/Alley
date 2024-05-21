package me.emmy.alley.party;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
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
        return parties.stream().filter(party -> party.getLeader().equals(uuid)).findFirst().orElse(null);
    }

    public Party getPartyMembers(UUID uuid) {
        return null;
    }
}
