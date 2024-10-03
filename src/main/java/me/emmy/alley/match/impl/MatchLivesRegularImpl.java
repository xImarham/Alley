package me.emmy.alley.match.impl;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.util.TaskUtil;
import me.emmy.alley.util.chat.Logger;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 5/21/2024
 */
public class MatchLivesRegularImpl extends MatchRegularImpl {

    private final Map<GameParticipant<MatchGamePlayerImpl>, Integer> lives = new HashMap<>();

    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    @Getter private GameParticipant<MatchGamePlayerImpl> winner;
    @Getter private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the MatchLivesRegularImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchLivesRegularImpl(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
        lives.put(participantA, 3);
        lives.put(participantB, 3);
    }

    @Override
    public boolean canStartRound() {
        return lives.get(participantA) > 0 && lives.get(participantB) > 0;
    }

    @Override
    public boolean canEndRound() {
        return participantA.isAllDead() || participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return lives.get(participantA) <= 0 || lives.get(participantB) <= 0;
    }

    /**
     * Reduces the lives of a participant by one.
     *
     * @param participant The participant whose lives are to be reduced.
     */
    public void reduceLife(GameParticipant<MatchGamePlayerImpl> participant) {
        lives.put(participant, lives.get(participant) - 1);
        if (lives.get(participant) <= 0) {
            determineWinnerAndLoser();
        }
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = participantA.containsPlayer(player.getUniqueId()) ? participantA : participantB;
        Logger.debug("Reducing life of " + participant.getPlayer().getPlayer().getName());
        reduceLife(participant);

        if (lives.get(participant) > 0) {
            TaskUtil.runTaskLater(() -> super.handleRespawn(player), 5L);
        } else {
            Logger.debug("super Handling death of " + participant.getPlayer().getPlayer().getName());
            super.handleDeath(player);
        }
    }

    /**
     * Determines the winner and loser of the match.
     */
    private void determineWinnerAndLoser() {
        if (lives.get(participantA) <= 0) {
            winner = participantB;
            loser = participantA;
        } else if (lives.get(participantB) <= 0) {
            winner = participantA;
            loser = participantB;
        }
    }
}