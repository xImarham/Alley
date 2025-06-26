package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.runnable.other.MatchRespawnRunnable;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 25/06/2025
 */
public class MatchCheckpointImpl extends MatchRegularImpl {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    /**
     * Constructor for the MatchRegularImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchCheckpointImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public boolean canEndRound() {
        return (this.participantA.isLostCheckpoint() || this.participantB.isLostCheckpoint())
                || (this.participantA.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.participantB.getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    @Override
    public void handleDeath(Player player) {
        super.handleDeath(player);

        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);

        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDead(false);
        }

        Location spawnLocation = this.getArena().getCenter();
        ListenerUtil.teleportAndClearSpawn(player, spawnLocation);

        new MatchRespawnRunnable(player, this, 1).runTaskTimer(this.plugin, 0L, 20L);
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);

        Location checkpoint = gamePlayer.getCheckpoint();
        if (checkpoint == null) {
            checkpoint = this.participantA.containsPlayer(player.getUniqueId()) ? getArena().getPos1() : getArena().getPos2();
        }

        player.teleport(checkpoint);

        this.giveLoadout(player, this.getKit());
        this.applyColorKit(player);
    }
}
