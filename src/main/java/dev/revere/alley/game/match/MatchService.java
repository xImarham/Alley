package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.match.impl.*;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public class MatchService {
    protected final Alley plugin;
    private final List<AbstractMatch> matches;

    private final List<String> blockedCommands;

    /**
     * Constructor for the MatchService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchService(Alley plugin) {
        this.plugin = plugin;
        this.matches = new CopyOnWriteArrayList<>();
        this.blockedCommands = new ArrayList<>();
        this.loadBlockedCommands();
    }

    /**
     * Creates a match with the given parameters.
     *
     * @param kit          The kit to be used in the match.
     * @param arena        The arena where the match will take place.
     * @param participantA The first participant in the match.
     * @param participantB The second participant in the match.
     */
    public void createAndStartMatch(Kit kit, AbstractArena arena, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB, boolean teamMatch, boolean affectStatistics, boolean isRanked) {
        Profile profileA = this.plugin.getProfileService().getProfile(participantA.getPlayers().get(0).getUuid());
        Profile profileB = this.plugin.getProfileService().getProfile(participantB.getPlayers().get(0).getUuid());
        if (profileA.getMatch() != null || profileB.getMatch() != null) {
            return;
        }

        Queue matchingQueue = this.plugin.getQueueService().getQueues().stream()
                .filter(queue -> queue.getKit().equals(kit))
                .findFirst()
                .orElse(null);

        AbstractMatch match;

        if (kit.isSettingEnabled(KitSettingLivesImpl.class)) {
            match = new MatchLivesImpl(matchingQueue, kit, arena, isRanked, participantA, participantB);
        } else if (kit.isSettingEnabled(KitSettingRoundsImpl.class)) {
            match = new MatchRoundsImpl(matchingQueue, kit, arena, isRanked, participantA, participantB, 3);
        } else if (kit.isSettingEnabled(KitSettingStickFightImpl.class)) {
            match = new MatchRoundsImpl(matchingQueue, kit, arena, isRanked, participantA, participantB, 5);
        } else if (kit.isSettingEnabled(KitSettingBedImpl.class)) {
            match = new MatchBedImpl(matchingQueue, kit, arena, isRanked, participantA, participantB);
        } else if (kit.isSettingEnabled(KitSettingCheckpointImpl.class)) {
            match = new MatchCheckpointImpl(matchingQueue, kit, arena, isRanked, participantA, participantB);
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

        Logger.log(this.matches.size() + " matches have been ended.");
    }

    private void loadBlockedCommands() {
        FileConfiguration config = this.plugin.getConfigService().getSettingsConfig();

        List<String> configBlockedCommands = config.getStringList("game.blocked-commands");
        if (configBlockedCommands.isEmpty()) {
            Logger.log("No blocked commands found in the configuration. Please check your settings.yml file.");
            return;
        }

        this.blockedCommands.addAll(configBlockedCommands);
    }
}
