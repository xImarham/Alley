package me.emmy.alley.match.impl;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.queue.Queue;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@Getter
public class MatchRegularImpl extends AbstractMatch {

    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    /**
     * Constructor for the RegularMatchImpl class.
     *
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchRegularImpl(Queue queue, Kit kit, Arena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);

        Location spawnLocation = participantA.containsPlayer(player.getUniqueId()) ? getMatchArena().getPos1() : getMatchArena().getPos2();
        player.teleport(spawnLocation);
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Arrays.asList(getParticipantA(), getParticipantB());
    }

    @Override
    public void handleRoundEnd() {
        Profile winner = participantA.isAllDead() ? Alley.getInstance().getProfileRepository().getProfile(participantB.getPlayer().getUuid()) : Alley.getInstance().getProfileRepository().getProfile(participantA.getPlayer().getUuid());
        Profile loser = participantA.isAllDead() ? Alley.getInstance().getProfileRepository().getProfile(participantA.getPlayer().getUuid()) : Alley.getInstance().getProfileRepository().getProfile(participantB.getPlayer().getUuid());

        winner.getProfileData().getKitData().get(getMatchKit().getName()).incrementWins();
        loser.getProfileData().getKitData().get(getMatchKit().getName()).incrementLosses();
        super.handleRoundEnd();
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return participantA.isAllDead() || participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return true;
    }
}
