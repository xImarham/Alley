package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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
    public boolean canEndRound() {
        return this.participantA.isAllEliminated() || this.participantB.isAllEliminated();
    }

    @Override
    public boolean canStartRound() {
        return !this.participantA.isAllEliminated() && !this.participantB.isAllEliminated();
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        if (participant.isBedBroken()) {
            super.handleDeath(player);
        } else {
            super.handleDeath(player);
            this.startRespawnProcess(player);
        }
    }

    @Override
    public void handleParticipant(Player player, MatchGamePlayerImpl gamePlayer) {
        GameParticipant<MatchGamePlayerImpl> gameParticipant = this.getParticipantA().containsPlayer(player.getUniqueId())
                ? this.getParticipantA()
                : this.getParticipantB();

        Bukkit.broadcastMessage(player.getName() + " died and their bed is " + (gameParticipant.isBedBroken() ? "broken" : "not broken"));
        if (gameParticipant.isBedBroken()) {
            GameParticipant<MatchGamePlayerImpl> participant = getParticipant(player);
            if (participant == null) {
                Bukkit.getLogger().warning("Participant not found for player: " + player.getName());
                return;
            }

            Bukkit.broadcastMessage(player.getName() + " is dead and set eliminated to true (should end game)");
            gamePlayer.setEliminated(true);
        }

        super.handleParticipant(player, gamePlayer);
    }

    @Override
    protected boolean shouldHandleRegularRespawn(Player player) {
        return false;
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        Location spawnLocation = this.getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        player.teleport(spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyWoolAndArmorColor(player);
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

    /**
     * Checks if a block is near a bed.
     *
     * @param block The block to check.
     * @return true if the block is near a bed, false otherwise.
     */
    public boolean isNearBed(Block block) {
        Location center = block.getLocation();
        for (int x = -8; x <= 1; x++) {
            for (int y = -8; y <= 1; y++) {
                for (int z = -8; z <= 1; z++) {
                    Block relativeBlock = new Location(block.getWorld(), center.getX() + x, center.getY() + y, center.getZ() + z).getBlock();
                    if (relativeBlock.getType() == Material.BED_BLOCK) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
