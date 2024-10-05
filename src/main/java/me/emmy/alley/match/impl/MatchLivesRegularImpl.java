package me.emmy.alley.match.impl;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.data.MatchGamePlayerData;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.util.TaskUtil;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.chat.Logger;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 5/21/2024
 */
public class MatchLivesRegularImpl extends MatchRegularImpl {

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
    }

    @Override
    public boolean canStartRound() {
        return participantA.getPlayer().getData().getLives() > 0 && participantB.getPlayer().getData().getLives() > 0;
    }

    @Override
    public boolean canEndRound() {
        return participantA.isAllDead() || participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return participantA.getPlayer().getData().getLives() <= 0 || participantB.getPlayer().getData().getLives() <= 0;
    }

    /**
     * Reduces the lives of a participant by one.
     *
     * @param participant The participant whose lives are to be reduced.
     */
    public void reduceLife(GameParticipant<MatchGamePlayerImpl> participant) {
        MatchGamePlayerData data = participant.getPlayer().getData();
        data.setLives(data.getLives() - 1);
        if (data.getLives() <= 0) {
            determineWinnerAndLoser();
        }
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = participantA.containsPlayer(player.getUniqueId()) ? participantA : participantB;
        Logger.debug("Reducing life of " + participant.getPlayer().getPlayer().getName());
        reduceLife(participant);

        if (participant.getPlayer().getData().getLives() > 0) {
            TaskUtil.runTaskLater(() -> startRespawnProcess(participant, player), 5L);
        } else {
            Logger.debug("Counting down for " + participant.getPlayer().getPlayer().getName());
            super.handleDeath(player);
        }
    }

    /**
     * Determines the winner and loser of the match.
     */
    private void determineWinnerAndLoser() {
        if (participantA.getPlayer().getData().getLives() <= 0) {
            winner = participantB;
            loser = participantA;
        } else if (participantB.getPlayer().getData().getLives() <= 0) {
            winner = participantA;
            loser = participantB;
        }
    }

    /**
     * Starts the respawn process for a participant.
     *
     * @param participant The participant to start the respawn process for.
     * @param player      The player to start the respawn process for.
     */
    private void startRespawnProcess(GameParticipant<MatchGamePlayerImpl> participant, Player player) {
        new BukkitRunnable() {
            int count = 3;
            @Override
            public void run() {
                if (count == 0) {
                    cancel();
                    superRespawn(participant, player);
                    return;
                }
                if (getMatchState() == EnumMatchState.ENDING_MATCH) {
                    cancel();
                    return;
                }
                player.sendMessage(CC.translate("&a" + count + "..."));
                count--;
            }
        }.runTaskTimer(Alley.getInstance(), 0L, 20L);
    }

    /**
     * Handles the death of a participant.
     *
     * @param participant The participant whose death is to be handled.
     * @param player      The player whose death is to be handled.
     */
    private void superRespawn(GameParticipant<MatchGamePlayerImpl> participant, Player player) {
        Logger.debug("super Handling death of " + participant.getPlayer().getPlayer().getName());
        super.handleRespawn(player);
    }
}