package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingLivesImpl;
import dev.revere.alley.feature.kit.setting.impl.KitSettingStickFightImpl;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.impl.MatchLivesImpl;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.impl.kit.MatchStickFightImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.tool.logger.Logger;
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
    protected final Alley plugin;
    private final List<AbstractMatch> matches;

    /**
     * Constructor for the MatchRepository class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchRepository(Alley plugin) {
        this.plugin = plugin;
        this.matches = new ArrayList<>();
    }

    /**
     * Creates a match with the given parameters.
     *
     * @param kit          The kit to be used in the match.
     * @param arena        The arena where the match will take place.
     * @param participantA The first participant in the match.
     * @param participantB The second participant in the match.
     */
    public void createAndStartMatch(Kit kit, AbstractArena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        Queue matchingQueue = this.plugin.getQueueService().getQueues().stream()
                .filter(queue -> queue.getKit().equals(kit) && !queue.isRanked())
                .findFirst()
                .orElse(null);

        AbstractMatch match;

        if (kit.isSettingEnabled(KitSettingLivesImpl.class)) {
            match = new MatchLivesImpl(matchingQueue, kit, arena, false, participantA, participantB);
        } else if (kit.isSettingEnabled(KitSettingBattleRushImpl.class)) {
            match = new MatchRoundsImpl(matchingQueue, kit, arena, false, participantA, participantB, 3);
        } else if (kit.isSettingEnabled(KitSettingStickFightImpl.class)) {
            match = new MatchStickFightImpl(matchingQueue, kit, arena, false, participantA, participantB, 5);
        } else {
            match = new MatchRegularImpl(matchingQueue, kit, arena, false, participantA, participantB);
        }

        match.startMatch();
    }

    public void endPresentMatches() {
        if (this.matches.isEmpty()) {
            return;
        }

        List<AbstractMatch> matchList = new ArrayList<>(this.matches);
        matchList.forEach(AbstractMatch::resetBlockChanges);

        Logger.log(this.matches.size() + " matches have been ended.");
    }
}
