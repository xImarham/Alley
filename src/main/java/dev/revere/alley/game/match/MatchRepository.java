package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBattleRushImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBedImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingLivesImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingStickFightImpl;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.impl.MatchBedImpl;
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
    public void createAndStartMatch(Kit kit, AbstractArena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, boolean teamMatch, boolean affectStatistics) {
        Queue matchingQueue = this.plugin.getQueueService().getQueues().stream()
                .filter(queue -> queue.getKit().equals(kit))
                .findFirst()
                .orElse(null);

        boolean isRanked = matchingQueue != null && matchingQueue.isRanked();

        AbstractMatch match;

        if (kit.isSettingEnabled(KitSettingLivesImpl.class)) {
            match = new MatchLivesImpl(matchingQueue, kit, arena, isRanked, participantA, participantB);
        } else if (kit.isSettingEnabled(KitSettingBattleRushImpl.class)) {
            match = new MatchRoundsImpl(matchingQueue, kit, arena, isRanked, participantA, participantB, 3);
        } else if (kit.isSettingEnabled(KitSettingStickFightImpl.class)) {
            match = new MatchStickFightImpl(matchingQueue, kit, arena, isRanked, participantA, participantB, 5);
        } else if (kit.isSettingEnabled(KitSettingBedImpl.class)) {
            match = new MatchBedImpl(matchingQueue, kit, arena, isRanked, participantA, participantB);
        } else {
            match = new MatchRegularImpl(matchingQueue, kit, arena, isRanked, participantA, participantB);
        }

        match.setTeamMatch(teamMatch);
        match.setAffectStatistics(affectStatistics);

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
