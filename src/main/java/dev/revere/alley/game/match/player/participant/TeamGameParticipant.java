package dev.revere.alley.game.match.player.participant;

import dev.revere.alley.game.match.player.GamePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamGameParticipant<T extends GamePlayer> extends GameParticipant<T> {
    private final List<T> players;

    /**
     * Constructor for the TeamGameParticipant class.
     *
     * @param t The player.
     */
    public TeamGameParticipant(T t) {
        super(t);

        this.players = new ArrayList<>();
        this.players.add(t);
    }

    @Override
    public List<T> getPlayers() {
        return this.players;
    }

    @Override
    public int getAliveCount() {
        int i = 0;

        for (GamePlayer gamePlayer : this.players) {
            if (!gamePlayer.isDead() && !gamePlayer.isDisconnected()) {
                i++;
            }
        }

        return i;
    }

    @Override
    public boolean isAllDead() {
        int i = 0;

        for (GamePlayer gamePlayer : this.players) {
            if (gamePlayer.isDead() || gamePlayer.isDisconnected()) {
                i++;
            }
        }

        return this.players.size() == i;
    }

    @Override
    public boolean containsPlayer(UUID uuid) {
        for (GamePlayer gamePlayer : this.players) {
            if (gamePlayer.getUuid().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getConjoinedNames() {
        StringBuilder builder = new StringBuilder();

        int size = this.players.size();
        if (size == 1) {
            return this.players.get(0).getUsername();
        }

        for (int i = 0; i < size; i++) {
            builder.append(this.players.get(i).getUsername());

            if (i == size - 2) {
                builder.append(" and ");
            } else if (i < size - 2) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }
}