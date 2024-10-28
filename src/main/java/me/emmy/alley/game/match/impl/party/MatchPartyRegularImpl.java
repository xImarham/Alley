package me.emmy.alley.game.match.impl.party;

import lombok.Getter;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.game.match.AbstractMatch;
import me.emmy.alley.game.match.player.GameParticipant;
import me.emmy.alley.game.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.party.Party;
import me.emmy.alley.queue.Queue;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 15:21
 */
@Getter
public class MatchPartyRegularImpl extends AbstractMatch {
    private final List<GameParticipant<MatchGamePlayerImpl>> teamA;
    private final List<GameParticipant<MatchGamePlayerImpl>> teamB;

    /**
     * Constructor for the MatchPartyRegularImpl class.
     *
     * @param queue The queue the match is being played in.
     * @param kit The kit the players are using.
     * @param arena The arena the match is being played in.
     * @param party The party the players are in.
     */
    public MatchPartyRegularImpl(Queue queue, Kit kit, Arena arena, Party party) {
        super(queue, kit, arena, false);

        List<Player> members = new ArrayList<>(party.getPlayersInParty());
        Collections.shuffle(members);

        int mid = members.size() / 2;
        teamA = createTeam(members.subList(0, mid));
        teamB = createTeam(members.subList(mid, members.size()));
    }

    private List<GameParticipant<MatchGamePlayerImpl>> createTeam(List<Player> players) {
        List<GameParticipant<MatchGamePlayerImpl>> team = new ArrayList<>();
        for (Player player : players) {
            team.add(new GameParticipant<>(new MatchGamePlayerImpl(player.getUniqueId(), player.getName())));
        }
        return team;
    }

    public void setupPlayer(Player player) {
        super.setupPlayer(player);

        for (GameParticipant<MatchGamePlayerImpl> participant : teamA) {
            if (participant.containsPlayer(player.getUniqueId())) {
                player.teleport(getArena().getPos1());
                return;
            }
        }

        for (GameParticipant<MatchGamePlayerImpl> participant : teamB) {
            if (participant.containsPlayer(player.getUniqueId())) {
                player.teleport(getArena().getPos2());
                return;
            }
        }
    }

    @Override
    public void handleRespawn(Player player) {

    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        List<GameParticipant<MatchGamePlayerImpl>> allParticipants = new ArrayList<>();
        allParticipants.addAll(teamA);
        allParticipants.addAll(teamB);
        return allParticipants;
    }

    @Override
    public boolean canStartRound() {
        return !teamA.isEmpty() && !teamB.isEmpty();
    }

    @Override
    public boolean canEndRound() {
        return false;
    }

    @Override
    public boolean canEndMatch() {
        return false;
    }
}