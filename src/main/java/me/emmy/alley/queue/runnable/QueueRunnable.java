package me.emmy.alley.queue.runnable;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.impl.RegularMatchImpl;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.queue.QueueProfile;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class QueueRunnable implements Runnable {

    /**
     * Method to run the queue runnable.
     */
    @Override
    public void run() {
        Alley.getInstance().getQueueRepository().getQueues().stream()
                .filter(queue -> queue.getProfiles().size() >= 2)
                .forEach(this::processQueue);
    }

    /**
     * Method to process the queue.
     *
     * @param queue The queue.
     */
    public void processQueue(Queue queue) {
        for (QueueProfile firstProfile : queue.getProfiles()) {
            Optional<Player> firstPlayerOpt = getPlayer(firstProfile);
            if (!firstPlayerOpt.isPresent()) {
                return;
            }

            for (QueueProfile secondProfile : queue.getProfiles()) {
                if (!firstProfile.equals(secondProfile)) {
                    Optional<Player> secondPlayerOpt = getPlayer(secondProfile);
                    if (!secondPlayerOpt.isPresent()) {
                        return;
                    }

                    clearQueueProfiles(queue, firstProfile, secondProfile);
                    GamePlayerList gamePlayerList = getGamePlayerList(firstPlayerOpt.get(), secondPlayerOpt.get());
                    GameParticipantList gameParticipantList = getGameParticipantList(gamePlayerList);
                    processGame(queue, gameParticipantList);
                }
            }
        }
    }

    /**
     * Method to get a player.
     *
     * @param profile The queue profile.
     * @return The player.
     */
    private Optional<Player> getPlayer(QueueProfile profile) {
        return Optional.ofNullable(Alley.getInstance().getServer().getPlayer(profile.getUuid()));
    }

    /**
     * Method to process the game.
     *
     * @param queue               The queue.
     * @param gameParticipantList The game participant list.
     */
    private void processGame(Queue queue, GameParticipantList gameParticipantList) {
        Arena arena = getArena(queue);
        AbstractMatch match = getMatchType(queue, gameParticipantList, arena);
        match.startMatch();
    }

    /**
     * Method to get the match type.
     *
     * @param queue               The queue.
     * @param gameParticipantList The game participant list.
     * @param arena               The arena.
     * @return The match type.
     */
    private @NotNull AbstractMatch getMatchType(Queue queue, GameParticipantList gameParticipantList, Arena arena) {
        return new RegularMatchImpl(queue, queue.getKit(), arena, gameParticipantList.getParticipantA(), gameParticipantList.getParticipantB());
    }

    /**
     * Method to get an arena.
     *
     * @param queue The queue.
     * @return The arena.
     */
    private Arena getArena(Queue queue) {
        return Alley.getInstance().getArenaRepository().getRandomArena(queue.getKit());
    }

    /**
     * Method to clear the queue profiles.
     *
     * @param queue         The queue.
     * @param firstProfile  The first profile.
     * @param secondProfile The second profile.
     */
    private void clearQueueProfiles(Queue queue, QueueProfile firstProfile, QueueProfile secondProfile) {
        queue.getProfiles().remove(firstProfile);
        queue.getProfiles().remove(secondProfile);
    }

    /**
     * Method to get the game participant list.
     *
     * @param gamePlayerList The game player list.
     * @return The game participant list.
     */
    private @NotNull GameParticipantList getGameParticipantList(GamePlayerList gamePlayerList) {
        return new GameParticipantList(
                new GameParticipant<>(gamePlayerList.getFirstMatchGamePlayer()),
                new GameParticipant<>(gamePlayerList.getSecondMatchGamePlayer())
        );
    }

    /**
     * Method to get the game player list.
     *
     * @param firstPlayer  The first player.
     * @param secondPlayer The second player.
     * @return The game player list.
     */
    private @NotNull GamePlayerList getGamePlayerList(Player firstPlayer, Player secondPlayer) {
        return new GamePlayerList(
                new MatchGamePlayerImpl(firstPlayer.getUniqueId(), firstPlayer.getName()),
                new MatchGamePlayerImpl(secondPlayer.getUniqueId(), secondPlayer.getName())
        );
    }

    @Getter
    private static class GameParticipantList {
        public final GameParticipant<MatchGamePlayerImpl> participantA;
        public final GameParticipant<MatchGamePlayerImpl> participantB;

        /**
         * Constructor for the GameParticipantList class.
         *
         * @param participantA The first participant.
         * @param participantB The second participant.
         */
        public GameParticipantList(GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
            this.participantA = participantA;
            this.participantB = participantB;
        }
    }

    @Getter
    private static class GamePlayerList {
        public final MatchGamePlayerImpl firstMatchGamePlayer;
        public final MatchGamePlayerImpl secondMatchGamePlayer;

        /**
         * Constructor for the GamePlayerList class.
         *
         * @param firstMatchGamePlayer  The first match game player.
         * @param secondMatchGamePlayer The second match game player.
         */
        public GamePlayerList(MatchGamePlayerImpl firstMatchGamePlayer, MatchGamePlayerImpl secondMatchGamePlayer) {
            this.firstMatchGamePlayer = firstMatchGamePlayer;
            this.secondMatchGamePlayer = secondMatchGamePlayer;
        }
    }
}
