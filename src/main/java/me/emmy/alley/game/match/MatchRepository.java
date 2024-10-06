package me.emmy.alley.game.match;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class MatchRepository {
    private final List<AbstractMatch> matches = new ArrayList<>();
}