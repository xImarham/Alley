package dev.revere.alley.game.match;

import dev.revere.alley.util.logger.Logger;
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

    public void endPresentMatches() {
        if (this.matches.isEmpty()) {
            return;
        }

        List<AbstractMatch> matchList = new ArrayList<>(this.matches);
        matchList.forEach(AbstractMatch::endMatch);

        Logger.log(this.matches.size() + " matches have been ended.");
    }
}
