package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.TaskUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 21/05/2025
 */
@Getter
public class MatchBedImpl extends MatchRegularImpl {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    /**
     * Constructor for the MatchBedImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchBedImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public boolean canEndMatch() {
        return (this.participantA.isAllDead() && !this.participantA.getPlayer().getData().isBedBroken())
                || (this.participantB.isAllDead() && !this.participantB.getPlayer().getData().isBedBroken());
    }

    @Override
    public boolean canEndRound() {
        return (this.participantA.isAllDead() && !this.participantA.getPlayer().getData().isBedBroken())
                || (this.participantB.isAllDead() && !this.participantB.getPlayer().getData().isBedBroken());
    }

    @Override
    public boolean canStartRound() {
        return (!this.participantA.isAllDead() && !this.participantA.getPlayer().getData().isBedBroken())
                && (!this.participantB.isAllDead() && !this.participantB.getPlayer().getData().isBedBroken());
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        if (participant.getPlayer().getData().isBedBroken()) {
            super.handleDeath(player);
        } else {
            TaskUtil.runTaskLater(() -> this.startRespawnProcess(player), 5L);
        }
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        Location spawnLocation = this.getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        player.teleport(spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyWoolAndArmorColor(player);

        this.notifyParticipants("&b" + player.getName() + " &ahas respawned");
        this.notifySpectators("&b" + player.getName() + " &ahas respawned");
    }

    /**
     * Alerts the participants about the bed destruction.
     *
     * @param breaker  The player who broke the bed.
     * @param opponent The opponent whose bed was destroyed.
     */
    public void alertBedDestruction(Player breaker, GameParticipant<MatchGamePlayerImpl> opponent) {
        String destructionMessage = "&6&lBED DESTRUCTION!";
        String subMessage = " &b" + breaker.getName() + " &7has destroyed the bed of &b" + opponent.getPlayer().getUsername() + "&7!";

        this.sendMessage(
                Arrays.asList(
                        "",
                        destructionMessage,
                        subMessage,
                        ""
                )
        );

        this.sendTitle(
                destructionMessage,
                subMessage,
                10, 70, 20
        );

        this.playSound(Sound.ENDERDRAGON_GROWL);
    }
}