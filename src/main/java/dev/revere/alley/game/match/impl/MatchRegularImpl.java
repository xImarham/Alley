package dev.revere.alley.game.match.impl;

import dev.revere.alley.elo.result.EloResult;
import dev.revere.alley.elo.result.OldEloResult;
import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.elo.EloCalculator;
import org.bukkit.Location;
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

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the RegularMatchImpl class.
     *
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public MatchRegularImpl(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked);
        this.participantA = participantA;
        this.participantB = participantB;
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);

        Location spawnLocation = participantA.containsPlayer(player.getUniqueId()) ? getArena().getPos1() : getArena().getPos2();
        player.teleport(spawnLocation);
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Arrays.asList(getParticipantA(), getParticipantB());
    }

    @Override
    public void handleRoundEnd() {
        winner = participantA.isAllDead() ? participantB : participantA;
        loser = participantA.isAllDead() ? participantA : participantB;
        loser.setEliminated(true);

        if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1 && isRanked()) {
            OldEloResult result = this.getOldEloResult();
            EloResult eloResult = this.getEloResult(result.oldWinnerElo, result.oldLoserElo);
            this.handleWinner(eloResult.newWinnerElo);
            this.handleLoser(eloResult.newLoserElo);
            this.sendEloResult(winner.getPlayer().getPlayer().getName(), loser.getPlayer().getPlayer().getName(), result.oldWinnerElo, result.oldLoserElo, eloResult.newWinnerElo, eloResult.newLoserElo);
        } else if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1 && !isRanked()) {
            this.handleUnrankedData();
        }
        super.handleRoundEnd();
    }

    private void sendEloResult(String winnerName, String loserName, int oldEloWinner, int oldEloLoser, int newEloWinner, int newEloLoser) {
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
    private @NotNull OldEloResult getOldEloResult() {
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
    private @NotNull EloResult getEloResult(int oldWinnerElo, int oldLoserElo) {
        int newWinnerElo = EloCalculator.determineWinnerAndCalculate(oldWinnerElo, oldLoserElo, true);
        int newLoserElo = EloCalculator.determineWinnerAndCalculate(oldLoserElo, oldWinnerElo, false);
        return new EloResult(newWinnerElo, newLoserElo);
    }

    /**
     * Method to handle the winner.
     *
     * @param elo The new elo of the winner.
     */
    private void handleWinner(int elo) {
        Profile winnerProfile = Alley.getInstance().getProfileRepository().getProfile(winner.getPlayer().getUuid());
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementRankedWins();
        winnerProfile.getProfileData().getProfileDivisionData().updateEloAndDivision(winnerProfile);
    }

    /**
     * Method to handle the loser.
     *
     * @param elo The new elo of the loser.
     */
    private void handleLoser(int elo) {
        Profile loserProfile = Alley.getInstance().getProfileRepository().getProfile(loser.getPlayer().getUuid());
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementRankedLosses();
        loserProfile.getProfileData().getProfileDivisionData().updateEloAndDivision(loserProfile);
    }

    /**
     * Method to handle unranked data (Incrementing global wins and losses).
     */
    private void handleUnrankedData() {
        Profile winnerProfile = Alley.getInstance().getProfileRepository().getProfile(winner.getPlayer().getUuid());
        winnerProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementUnrankedWins();

        Profile loserProfile = Alley.getInstance().getProfileRepository().getProfile(loser.getPlayer().getUuid());
        loserProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementUnrankedLosses();
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return participantA.isAllDead() || participantB.isAllDead();
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
