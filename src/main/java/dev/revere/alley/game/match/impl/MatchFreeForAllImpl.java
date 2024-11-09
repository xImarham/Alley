package dev.revere.alley.game.match.impl;

import dev.revere.alley.arena.Arena;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.queue.Queue;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2024 - 09:50
 */
public class MatchFreeForAllImpl extends AbstractMatch {
    private final List<GameParticipant<MatchGamePlayerImpl>> participants;

    public MatchFreeForAllImpl(Queue queue, Kit kit, Arena arena, List<GameParticipant<MatchGamePlayerImpl>> participants) {
        super(queue, kit, arena, false);
        this.participants = participants;
    }

    public void setupPlayer(Player player) {
        super.setupPlayer(player);
        player.teleport(getArena().getPos1());
    }

    @Override
    public void handleRespawn(Player player) {

    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Collections.emptyList();
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return false;
    }

    @Override
    public boolean canEndMatch() {
        return false;
    }
}
