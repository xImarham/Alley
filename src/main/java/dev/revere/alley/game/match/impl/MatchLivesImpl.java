package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public class MatchLivesImpl extends MatchRegularImpl {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the MatchLivesImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchLivesImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public boolean canStartRound() {
        return participantA.getLeader().getData().getLives() > 0 && participantB.getLeader().getData().getLives() > 0;
    }

    @Override
    public boolean canEndRound() {
        return (participantA.isAllEliminated() || participantB.isAllEliminated())
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    /**
     * Reduces the life count of a player in the match.
     *
     * @param data The MatchGamePlayerData of the player whose life is to be reduced.
     */
    public void reduceLife(MatchGamePlayerData data) {
        data.setLives(data.getLives() - 1);
    }

    @Override
    public void handleParticipant(Player player, MatchGamePlayerImpl gamePlayer) {
        MatchGamePlayerData data = this.getGamePlayer(player).getData();
        this.reduceLife(data);

        if (data.getLives() <= 0) {
            gamePlayer.setEliminated(true);
        }

        super.handleParticipant(player, gamePlayer);
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        Location spawnLocation = this.getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        player.teleport(spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyColorKit(player);
    }
}