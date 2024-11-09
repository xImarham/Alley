package dev.revere.alley.elo.result;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2024 - 15:24
 */
public class EloResult {
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