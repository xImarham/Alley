package dev.revere.alley.game.match.impl;

import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class MatchFFAImpl extends AbstractMatch {
    private final List<GameParticipant<MatchGamePlayerImpl>> participants;
    private GameParticipant<MatchGamePlayerImpl> winner;

    /**
     * Constructor for the MatchFFAImpl class.
     *
     * @param queue        The queue associated with this match.
     * @param kit          The kit used in this match.
     * @param arena        The arena where the match takes place.
     * @param participants The list of participants in the match.
     */
    public MatchFFAImpl(Queue queue, Kit kit, AbstractArena arena, List<GameParticipant<MatchGamePlayerImpl>> participants) {
        super(queue, kit, arena, false);
        this.participants = new ArrayList<>(participants);
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);

        Location spawn = this.getArena().getPos1();
        player.teleport(spawn);
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();

        player.teleport(this.getArena().getCenter());
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return participants;
    }

    @Override
    public boolean canStartRound() {
        return participants.stream().noneMatch(GameParticipant::isAllDead);
    }

    @Override
    public boolean canEndRound() {
        long aliveCount = participants.stream().filter(p -> !p.isAllDead()).count();
        return aliveCount <= 1;
    }

    @Override
    public boolean canEndMatch() {
        return this.canEndRound();
    }

    @Override
    public void handleRoundEnd() {
        super.handleRoundEnd();

        this.participants.stream()
            .filter(participant -> !participant.isAllDead())
            .findFirst()
            .ifPresent(remaining -> {
                this.winner = remaining;
                this.winner.setEliminated(false);

                // temporarily, couldnt be asked to mess with clickables again

                this.sendMessage("Winner: " + this.winner.getPlayer().getUsername());

                String losers = this.participants.stream()
                    .filter(participant -> participant != this.winner)
                    .map(GameParticipant::getPlayer)
                    .map(MatchGamePlayerImpl::getUsername)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("None");

                this.sendMessage("Losers: " + losers);
            })
        ;
    }
}
