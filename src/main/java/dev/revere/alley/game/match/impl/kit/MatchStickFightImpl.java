package dev.revere.alley.game.match.impl.kit;

import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.MatchUtility;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.snapshot.Snapshot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Alley
 * @since 07/03/2025
 */
@Getter
public class MatchStickFightImpl extends MatchRoundsRegularImpl {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    private final int rounds;
    private int currentRound;

    /**
     * Constructor for the RegularMatchImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     * @param rounds       The amount of rounds the match will have.
     */
    public MatchStickFightImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, int rounds) {
        super(queue, kit, arena, ranked, participantA, participantB, rounds);
        this.participantA = participantA;
        this.participantB = participantB;
        this.rounds = rounds;
    }

    @Override
    public boolean canStartRound() {
        return this.participantA.getPlayer().getData().getGoals() < this.rounds && this.participantB.getPlayer().getData().getGoals() < this.rounds;
    }

    @Override
    public boolean canEndRound() {
        return this.participantA.isAllDead() || this.participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return this.participantA.getPlayer().getData().getGoals() == this.rounds || this.participantB.getPlayer().getData().getGoals() == this.rounds;
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        participant.getPlayer().getData().incrementDeaths();
        participant.getPlayer().setDead(true);

        this.handleRoundEnd(player);
    }

    @Override
    public void handleRespawn(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        participant.getPlayer().setDead(false);
    }

    public void handleRoundEnd(Player player) {
        this.winner = this.participantA.isAllDead() ? this.participantB : this.participantA;
        this.winner.getPlayer().getData().incrementGoals();
        this.loser = this.participantA.isAllDead() ? this.participantA : this.participantB;
        this.currentRound++;

        this.broadcastScoredMessage(this.winner, this.loser);
        this.removePlacedBlocks();

        if (this.canEndMatch()) {
            this.setEndTime(System.currentTimeMillis());
            this.setState(EnumMatchState.ENDING_MATCH);
            this.getRunnable().setStage(4);

            if (this.participantA.getPlayers().size() == 1 && this.participantB.getPlayers().size() == 1) {
                MatchUtility.sendMatchResult(this, winner.getPlayer().getPlayer().getName(), loser.getPlayer().getPlayer().getName());
            }

            this.handleData(this.winner, this.loser, this.participantA, this.participantB);

            this.getParticipants().forEach(gameParticipant -> {
                if (gameParticipant.isAllDead()) {
                    gameParticipant.getPlayers().forEach(gamePlayer -> {
                        Player player1 = gamePlayer.getPlayer();
                        if (player1 != null && !gamePlayer.isDead()) {
                            Snapshot snapshot = new Snapshot(player1, true);
                            this.getSnapshots().add(snapshot);
                        }
                    });
                }
            });

            return;
        }

        this.handleRespawn(player);
        this.setState(EnumMatchState.ENDING_ROUND);

        this.getParticipants().forEach(participant -> participant.getPlayers().forEach(playerParticipant -> {
            Player player1 = playerParticipant.getPlayer();
            player1.setVelocity(new Vector(0, 0, 0));
            playerParticipant.setDead(false);

            super.setupPlayer(player1);
        }));
    }
}