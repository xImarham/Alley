package dev.revere.alley.game.match.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.elo.EloCalculator;
import dev.revere.alley.feature.elo.result.EloResult;
import dev.revere.alley.feature.elo.result.OldEloResult;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.utility.MatchUtility;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class MatchRegularImpl extends AbstractMatch {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    public final ChatColor teamAColor;
    public final ChatColor teamBColor;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the RegularMatchImpl class.
     *
     * @param queue       The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked      Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchRegularImpl(Queue queue, Kit kit, AbstractArena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked);
        this.participantA = participantA;
        this.participantB = participantB;
        this.teamAColor = ChatColor.BLUE;
        this.teamBColor = ChatColor.RED;
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);
        this.applyWoolColor(player);

        Location spawnLocation = this.participantA.containsPlayer(player.getUniqueId()) ? getArena().getPos1() : getArena().getPos2();
        player.teleport(spawnLocation);
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Arrays.asList(this.participantA, this.participantB);
    }

    /**
     * Get the team color of a participant.
     *
     * @param participant The participant to get the team color of.
     * @return The team color of the participant.
     */
    public ChatColor getTeamColor(GameParticipant<MatchGamePlayerImpl> participant) {
        return participant == this.participantA ? this.teamAColor : this.teamBColor;
    }

    /**
     * Applies the wool color to the player based on their team.
     *
     * @param player The player to apply the wool color to.
     */
    public void applyWoolColor(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        if (participant == null) {
            return;
        }

        int woolColor = (participant == this.participantA) ? 11 : 14;

        participant.getPlayers().forEach(gamePlayer -> {
            Player teamPlayer = gamePlayer.getPlayer();

            teamPlayer.getInventory().all(Material.WOOL).forEach((key, value) -> {
                teamPlayer.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(woolColor).amount(64).build());
            });

            teamPlayer.updateInventory();
        });
    }

    @Override
    public void handleRoundEnd() {
        this.winner = this.participantA.isAllDead() ? this.participantB : this.participantA;
        this.loser = this.participantA.isAllDead() ? this.participantA : this.participantB;
        this.loser.setEliminated(true);

        if (this.participantA.getPlayers().size() == 1 && this.participantB.getPlayers().size() == 1) {
            MatchUtility.sendMatchResult(this, this.winner.getPlayer().getPlayer().getName(), this.loser.getPlayer().getPlayer().getName());
        } else {
            MatchUtility.sendConjoinedMatchResult(this, this.winner, this.loser);
        }

        if (!this.isRanked()) {
            this.sendProgressToWinner(this.winner.getPlayer().getPlayer());
        }

        this.handleData(this.winner, this.loser, this.participantA, this.participantB);
        super.handleRoundEnd();
    }

    /**
     * Method to handle the data of the match such as elo changes and unranked stats.
     *
     * @param winner      The winner of the match.
     * @param loser       The loser of the match.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public void handleData(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1 && isRanked()) {
            OldEloResult result = this.getOldEloResult(winner, loser);
            EloResult eloResult = this.getEloResult(result.oldWinnerElo, result.oldLoserElo);

            this.handleWinner(eloResult.newWinnerElo, winner);
            this.handleLoser(eloResult.newLoserElo, loser);

            this.sendEloResult(winner.getPlayer().getPlayer().getName(), loser.getPlayer().getPlayer().getName(), result.oldWinnerElo, result.oldLoserElo, eloResult.newWinnerElo, eloResult.newLoserElo);
        } else if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1 && !isRanked()) {
            ProfileService profileService = Alley.getInstance().getProfileService();

            Profile winnerProfile = profileService.getProfile(winner.getPlayer().getUuid());
            winnerProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementWins();
            winnerProfile.getProfileData().incrementUnrankedWins();

            Profile loserProfile = profileService.getProfile(loser.getPlayer().getUuid());
            loserProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementLosses();
            loserProfile.getProfileData().incrementUnrankedLosses();
        }
    }

    /**
     * Sends the elo result message.
     *
     * @param winnerName The name of the winner.
     * @param loserName  The name of the loser.
     * @param oldEloWinner The old elo of the winner.
     * @param oldEloLoser The old elo of the loser.
     * @param newEloWinner The new elo of the winner.
     * @param newEloLoser The new elo of the loser.
     */
    public void sendEloResult(String winnerName, String loserName, int oldEloWinner, int oldEloLoser, int newEloWinner, int newEloLoser) {
        FileConfiguration config = Alley.getInstance().getConfigService().getMessagesConfig();

        List<String> list = config.getStringList("match.ended.elo-changes.format");
        String winnerIndicatorColor = config.getString("match.ended.elo-changes.winner-indicator-color", "&a");
        String loserIndicatorColor = config.getString("match.ended.elo-changes.loser-indicator-color", "&c");

        list.replaceAll(string -> string
                .replace("{winner}", winnerName)
                .replace("{loser}", loserName)
                .replace("{old-winner-elo}", String.valueOf(oldEloWinner))
                .replace("{old-loser-elo}", String.valueOf(oldEloLoser))
                .replace("{new-winner-elo}", String.valueOf(newEloWinner))
                .replace("{new-loser-elo}", String.valueOf(newEloLoser))
                .replace("{winner-indicator}", newEloWinner > oldEloWinner ? "+" : "-")
                .replace("{loser-indicator}", newEloLoser > oldEloLoser ? "+" : "-")
                .replace("{winner-color}", newEloWinner > oldEloWinner ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{loser-color}", newEloLoser > oldEloLoser ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{math-winner-elo}", String.valueOf(Math.abs(oldEloWinner - newEloWinner)))
                .replace("{math-loser-elo}", String.valueOf(Math.abs(oldEloLoser - newEloLoser)))
        );

        list.forEach(this::notifyParticipants);
    }

    /**
     * Method to get the old elo result.
     *
     * @return The old elo result.
     */
    public @NotNull OldEloResult getOldEloResult(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        int oldWinnerElo = winner.getPlayer().getElo();
        int oldLoserElo = loser.getPlayer().getElo();
        return new OldEloResult(oldWinnerElo, oldLoserElo);
    }

    /**
     * Method to get the elo result.
     *
     * @param oldWinnerElo The old elo of the winner.
     * @param oldLoserElo  The old elo of the loser.
     * @return The elo result.
     */
    public @NotNull EloResult getEloResult(int oldWinnerElo, int oldLoserElo) {
        EloCalculator eloCalculator = Alley.getInstance().getEloCalculator();
        int newWinnerElo = eloCalculator.determineNewElo(oldWinnerElo, oldLoserElo, true);
        int newLoserElo = eloCalculator.determineNewElo(oldLoserElo, oldWinnerElo, false);
        return new EloResult(newWinnerElo, newLoserElo);
    }

    /**
     * Method to handle the winner.
     *
     * @param elo The new elo of the winner.
     * @param winner The winner of the match.
     */
    public void handleWinner(int elo, GameParticipant<MatchGamePlayerImpl> winner) {
        Profile winnerProfile = Alley.getInstance().getProfileService().getProfile(winner.getPlayer().getUuid());
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementRankedWins();
        winnerProfile.getProfileData().updateElo(winnerProfile);
    }

    /**
     * Method to handle the loser.
     *
     * @param elo The new elo of the loser.
     * @param loser The loser of the match.
     */
    public void handleLoser(int elo, GameParticipant<MatchGamePlayerImpl> loser) {
        Profile loserProfile = Alley.getInstance().getProfileService().getProfile(loser.getPlayer().getUuid());
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementRankedLosses();
        loserProfile.getProfileData().updateElo(loserProfile);
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return this.participantA.isAllDead() || this.participantB.isAllDead();
    }

    @Override
    public boolean canEndMatch() {
        return true;
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();
        PlayerUtil.reset(player, false);
    }
}