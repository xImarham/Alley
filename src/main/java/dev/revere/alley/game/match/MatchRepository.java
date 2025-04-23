package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingStickFightImpl;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.game.match.impl.MatchLivesRegularImpl;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.impl.MatchRoundsRegularImpl;
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
    private final List<AbstractMatch> matches;

    public MatchRepository() {
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
        for (Queue queue : Alley.getInstance().getQueueService().getQueues()) {
            if (queue.getKit().equals(kit) && !queue.isRanked()) {
                if (queue.getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                    AbstractMatch match = new MatchLivesRegularImpl(queue, kit, arena, false, participantA, participantB);
                    match.startMatch();
                } else if (queue.getKit().isSettingEnabled(KitSettingBattleRushImpl.class)) {
                    AbstractMatch match = new MatchRoundsRegularImpl(queue, kit, arena, false, participantA, participantB, 3);
                    match.startMatch();
                } else if (queue.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) {
                    AbstractMatch match = new MatchStickFightImpl(queue, kit, arena, false, participantA, participantB, 5);
                    match.startMatch();
                } else {
                    AbstractMatch match = new MatchRegularImpl(queue, kit, arena, false, participantA, participantB);
                    match.startMatch();
                }
            }
        }
    }

    public void endPresentMatches() {
        if (this.matches.isEmpty()) {
            return;
        }

        List<AbstractMatch> matchList = new ArrayList<>(this.matches);
        matchList.forEach(AbstractMatch::endMatchOnServerStop);

        Logger.log(this.matches.size() + " matches have been ended.");
    }
}
