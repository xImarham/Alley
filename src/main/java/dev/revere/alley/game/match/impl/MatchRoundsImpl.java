package dev.revere.alley.game.match.impl;

import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingStickFightImpl;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
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
public class MatchRoundsImpl extends MatchRegularImpl {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    private final int rounds;
    private int currentRound;

    @Setter
    private String scorer;
    private Player fallenPlayer;

    /**
     * Constructor for the MatchRoundsImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     * @param rounds       The amount of rounds the match will have.
     */
    public MatchRoundsImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, int rounds) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.participantA = participantA;
        this.participantB = participantB;
        this.rounds = rounds;
        this.scorer = "Unknown";
    }

    @Override
    public void handleRoundEnd() {
        this.winner = this.participantA.isAllDead() ? this.participantB : this.participantA;
        this.winner.getPlayer().getData().incrementScore();
        this.loser = this.participantA.isAllDead() ? this.participantA : this.participantB;
        this.currentRound++;

        this.broadcastPlayerScoreMessage(this.winner, this.loser, this.scorer);

        if (this.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) {
            if (this.canEndMatch()) {
                this.removePlacedBlocks();
                this.setEndTime(System.currentTimeMillis());
                this.setState(EnumMatchState.ENDING_MATCH);
                this.getRunnable().setStage(4);
                super.handleRoundEnd();
            } else {

                this.removePlacedBlocks();
                this.handleRespawn(this.fallenPlayer);
                this.setState(EnumMatchState.ENDING_ROUND);

                this.getParticipants().forEach(participant -> participant.getPlayers().forEach(playerParticipant -> {
                    Player player1 = playerParticipant.getPlayer();
                    player1.setVelocity(new Vector(0, 0, 0));
                    playerParticipant.setDead(false);

                    super.setupPlayer(player1);
                }));
            }
        } else {
            if (this.canEndMatch()) {
                super.handleRoundEnd();
            } else {
                this.removePlacedBlocks();
                this.setState(EnumMatchState.ENDING_ROUND);

                this.getParticipants().forEach(participant -> participant.getPlayers().forEach(playerParticipant -> {
                    Player player = playerParticipant.getPlayer();
                    player.setVelocity(new Vector(0, 0, 0));
                    playerParticipant.setDead(false);

                    super.setupPlayer(player);
                }));
            }
        }
    }

    @Override
    public void handleDeath(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.participantA.containsPlayer(player.getUniqueId()) ? this.participantA : this.participantB;
        participant.getPlayer().getData().incrementDeaths();

        this.fallenPlayer = player;

        this.startRespawnProcess(player);
    }

    @Override
    public void startRespawnProcess(Player player) {
        if (this.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) {
            GameParticipant<MatchGamePlayerImpl> participant;

            if (this.participantA.containsPlayer(player.getUniqueId())) {
                participant = this.participantA;
            } else {
                participant = this.participantB;
            }

            if (participant instanceof TeamGameParticipant<?>) {
                TeamGameParticipant<MatchGamePlayerImpl> team = (TeamGameParticipant<MatchGamePlayerImpl>) participant;
                MatchGamePlayerImpl gamePlayer = team.getPlayers().stream()
                        .filter(gamePlayer1 -> gamePlayer1.getUuid().equals(player.getUniqueId()))
                        .findFirst()
                        .orElse(null);

                if (gamePlayer != null) {
                    gamePlayer.getData().incrementDeaths();
                    gamePlayer.setDead(true);
                    this.handleRoundEnd();
                }
            } else {
                MatchGamePlayerImpl gamePlayer = participant.getPlayer();
                gamePlayer.getData().incrementDeaths();
                gamePlayer.setDead(true);
                this.handleRoundEnd();
            }
        } else {
            super.startRespawnProcess(player);
        }
    }

    @Override
    public void handleRespawn(Player player) {
        PlayerUtil.reset(player, true);

        Location spawnLocation = getParticipants().get(0).containsPlayer(player.getUniqueId()) ? this.getArena().getPos1() : this.getArena().getPos2();
        player.teleport(spawnLocation);

        this.giveLoadout(player, this.getKit());
        this.applyWoolAndArmorColor(player);
    }

    @Override
    public boolean canStartRound() {
        return this.participantA.getPlayer().getData().getScore() < this.rounds && this.participantB.getPlayer().getData().getScore() < this.rounds;
    }

    @Override
    public boolean canEndRound() {
        return this.participantA.isAllDead() || this.participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return this.participantA.getPlayer().getData().getScore() == this.rounds || this.participantB.getPlayer().getData().getScore() == this.rounds;
    }

    /**
     * Broadcasts a message to all players in the match when a player scores.
     *
     * @param winner The player who scored.
     * @param loser  The player who was scored on.
     * @param scorer The name of the player who scored.
     */
    public void broadcastPlayerScoreMessage(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser, String scorer) {
        ChatColor teamWinnerColor = this.getTeamColor(winner);
        ChatColor teamLoserColor = this.getTeamColor(loser);

        String configPath = this.isTeamMatch() ? "match.scored.format.team" : "match.scored.format.solo";

        FileConfiguration config = this.plugin.getConfigService().getMessagesConfig();
        if (config.getBoolean("match.scored.enabled")) {
            for (String message : config.getStringList(configPath)) {
                this.notifyAll(message
                        .replace("{scorer}", scorer)
                        .replace("{winner}", winner.getPlayer().getUsername())
                        .replace("{winner-color}", teamWinnerColor.toString())
                        .replace("{winner-goals}", String.valueOf(winner.getPlayer().getData().getScore()))
                        .replace("{loser}", loser.getPlayer().getUsername())
                        .replace("{loser-color}", teamLoserColor.toString())
                        .replace("{loser-goals}", String.valueOf(loser.getPlayer().getData().getScore()))
                );
            }
        }
    }
}