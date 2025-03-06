package dev.revere.alley.game.match.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
@Getter
public class MatchRoundsRegularImpl extends MatchRegularImpl {
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
     */
    public MatchRoundsRegularImpl(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, int rounds) {
        super(queue, kit, arena, ranked, participantA, participantB);
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
    public void handleRoundEnd() {
        this.winner = this.participantA.isAllDead() ? this.participantB : this.participantA;
        this.winner.getPlayer().getData().incrementGoals();
        this.loser = this.participantA.isAllDead() ? this.participantA : this.participantB;

        this.broadcastScoredMessage();

        if (this.canEndMatch()) {
            super.handleRoundEnd();
        } else {
            this.removePlacedBlocks();

            this.currentRound++;
            this.setState(EnumMatchState.ENDING_ROUND);
            this.getRunnable().setStage(1);

            this.getParticipants().forEach(participant -> participant.getPlayers().forEach(playerParticipant -> {
                Player player = playerParticipant.getPlayer();
                player.setVelocity(new Vector(0, 0, 0));
                playerParticipant.setDead(false);

                super.setupPlayer(player);
            }));
        }
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        Location spawnLocation = getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        player.teleport(spawnLocation);

        player.getInventory().setArmorContents(this.getKit().getArmor());
        player.getInventory().setContents(this.getKit().getInventory());

        this.notifyParticipants("&b" + player.getName() + " &ahas respawned");
        this.notifySpectators("&b" + player.getName() + " &ahas respawned");
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        participant.getPlayer().getData().incrementDeaths();

        this.startRespawnProcess(player);
    }

    private void broadcastScoredMessage() {
        ChatColor teamWinnerColor = this.getTeamColor(this.winner);
        ChatColor teamLoserColor = this.getTeamColor(this.loser);

        FileConfiguration config = Alley.getInstance().getConfigService().getMessagesConfig();

        if (config.getBoolean("match.scored.enabled")) {
            for (String message : config.getStringList("match.scored.format")) {
                this.notifyAll(message
                        .replace("{winner-color}", teamWinnerColor.toString())
                        .replace("{winner}", this.winner.getPlayer().getUsername())
                        .replace("{winner-goals}", String.valueOf(this.winner.getPlayer().getData().getGoals()))
                        .replace("{loser-color}", teamLoserColor.toString())
                        .replace("{loser}", this.loser.getPlayer().getUsername())
                        .replace("{loser-goals}", String.valueOf(this.loser.getPlayer().getData().getGoals()))
                );
            }
        }
    }
}