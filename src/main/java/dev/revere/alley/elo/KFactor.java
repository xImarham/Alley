package dev.revere.alley.elo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@Getter
@RequiredArgsConstructor
public class KFactor {
    private final int startElo;
    private final int endElo;
    private final int kFactor;
}