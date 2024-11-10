package dev.revere.alley.elo;

import lombok.experimental.UtilityClass;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@UtilityClass
public class EloManager {
    private final KFactor[] K_FACTORS = {
            new KFactor(0, 1100, 25),
            new KFactor(1001, 1400, 20),
            new KFactor(1401, 1800, 15),
            new KFactor(1801, 2200, 10),

    };

    private final int K_FACTOR = 25;

    /**
     * Method to calculate the elo.
     *
     * @param winnerElo The elo of the winner.
     * @param loserElo  The elo of the loser.
     * @param won       If the player won.
     * @return The new elo.
     */
    public int calculateElo(int winnerElo, int loserElo, boolean won) {
        return won ? calculateElo(winnerElo, loserElo, 1) : calculateElo(winnerElo, loserElo, 0);
    }

    /**
     * Method to calculate the elo.
     *
     * @param winnerElo The elo of the winner.
     * @param loserElo  The elo of the loser.
     * @param score     The score of the player.
     * @return The new elo.
     */
    public int calculateElo(int winnerElo, int loserElo, int score) {
        double kFactor = getKFactor(winnerElo);
        double expectedScore = calculateExpectedScore(winnerElo, loserElo);
        int newElo = calculateNewElo(winnerElo, score, expectedScore, kFactor);

        if (score == 1 && newElo == winnerElo) {
            newElo++;
        }
        return newElo;
    }

    /**
     * Method to calculate the new elo.
     *
     * @param elo          The elo of the player.
     * @param score        The score of the player.
     * @param expectedScore The expected score of the player.
     * @param kFactor      The K factor of the player.
     * @return The new elo.
     */
    private int calculateNewElo(int elo, int score, double expectedScore, double kFactor) {
        return (int) (elo + (kFactor * (score - expectedScore)));
    }

    /**
     * Method to calculate the expected score.
     *
     * @param winnerElo The elo of the winner.
     * @param loserElo  The elo of the loser.
     * @return The expected score.
     */
    private double calculateExpectedScore(int winnerElo, int loserElo) {
        return 1 / (1 + Math.pow(10, (double) (loserElo - winnerElo) / 400));
    }

    /**
     * Method to get the K factor.
     *
     * @param elo The elo of the player.
     * @return The K factor.
     */
    private double getKFactor(int elo) {
        for (KFactor kFactor : K_FACTORS) {
            if (elo >= kFactor.getStartElo() && elo <= kFactor.getEndElo()) {
                return kFactor.getKFactor();
            }
        }
        return K_FACTOR;
    }
}
