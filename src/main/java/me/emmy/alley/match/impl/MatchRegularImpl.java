package me.emmy.alley.match.impl;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.util.elo.EloManager;
import org.bukkit.Location;
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

        Location spawnLocation = participantA.containsPlayer(player.getUniqueId()) ? getMatchArena().getPos1() : getMatchArena().getPos2();
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
            OldEloResult result = getOldEloResult();
            EloResult eloResult = getEloResult(result.oldWinnerElo, result.oldLoserElo);
            handleWinner(eloResult.newWinnerElo);
            handleLoser(eloResult.newLoserElo);
        }
        super.handleRoundEnd();
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

    private static class OldEloResult {
        public final int oldWinnerElo;
        public final int oldLoserElo;

        /**
         * Constructor for the OldEloResult class.
         *
         * @param oldWinnerElo The old elo of the winner.
         * @param oldLoserElo  The old elo of the loser.
         */
        public OldEloResult(int oldWinnerElo, int oldLoserElo) {
            this.oldWinnerElo = oldWinnerElo;
            this.oldLoserElo = oldLoserElo;
        }
    }

    /**
     * Method to get the elo result.
     *
     * @param oldWinnerElo The old elo of the winner.
     * @param oldLoserElo  The old elo of the loser.
     * @return The elo result.
     */
    private @NotNull EloResult getEloResult(int oldWinnerElo, int oldLoserElo) {
        int newWinnerElo = EloManager.calculateElo(oldWinnerElo, oldLoserElo, true);
        int newLoserElo = EloManager.calculateElo(oldLoserElo, oldWinnerElo, false);
        return new EloResult(newWinnerElo, newLoserElo);
    }

    private static class EloResult {
        public final int newWinnerElo;
        public final int newLoserElo;

        /**
         * Constructor for the EloResult class.
         *
         * @param newWinnerElo The new elo of the winner.
         * @param newLoserElo  The new elo of the loser.
         */
        public EloResult(int newWinnerElo, int newLoserElo) {
            this.newWinnerElo = newWinnerElo;
            this.newLoserElo = newLoserElo;
        }
    }

    /**
     * Method to handle the winner.
     *
     * @param elo The new elo of the winner.
     */
    private void handleWinner(int elo) {
        Profile winnerProfile = Alley.getInstance().getProfileRepository().getProfile(winner.getPlayer().getUuid());
        winnerProfile.getProfileData().getKitData().get(getMatchKit().getName()).setElo(elo);
        winnerProfile.getProfileData().getKitData().get(getMatchKit().getName()).incrementWins();
        winnerProfile.getProfileData().getProfileDivisionData().updateEloAndDivision(winnerProfile);
    }

    /**
     * Method to handle the loser.
     *
     * @param elo The new elo of the loser.
     */
    private void handleLoser(int elo) {
        Profile loserProfile = Alley.getInstance().getProfileRepository().getProfile(loser.getPlayer().getUuid());
        loserProfile.getProfileData().getKitData().get(getMatchKit().getName()).setElo(elo);
        loserProfile.getProfileData().getKitData().get(getMatchKit().getName()).incrementLosses();
        loserProfile.getProfileData().getProfileDivisionData().updateEloAndDivision(loserProfile);
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
}
