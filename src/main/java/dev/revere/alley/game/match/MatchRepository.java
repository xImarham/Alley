package dev.revere.alley.game.match;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public class MatchRepository {
    private final List<AbstractMatch> matches;

    public MatchRepository() {
        this.matches = new ArrayList<>();
    }
}