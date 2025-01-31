package dev.revere.alley.util.elo.result;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2024 - 15:23
 */
public class OldEloResult {
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