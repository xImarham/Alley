package me.emmy.alley.match;

import lombok.experimental.UtilityClass;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 24/09/2024 - 17:12
 */
@UtilityClass
public class MatchUtility {
    /**
     * Intentionally made to deny player movement during a match countdown.
     *
     * @param participants the participants
     * @param player       the player
     * @param match        the match
     */
    public void denyPlayerMovement(List<GameParticipant<MatchGamePlayerImpl>> participants, Player player, AbstractMatch match) {
        if (participants.size() == 2) {
            GameParticipant<?> participantA = participants.get(0);
            GameParticipant<?> participantB = participants.get(1);

            Player playerA = participantA.getPlayer().getPlayer();
            Player playerB = participantB.getPlayer().getPlayer();

            Location playerLocation = player.getLocation();
            Location locationA = match.getMatchArena().getPos1();
            Location locationB = match.getMatchArena().getPos2();

            if (player.equals(playerA)) {
                if (playerLocation.getBlockX() != locationA.getBlockX() || playerLocation.getBlockZ() != locationA.getBlockZ()) {
                    player.teleport(new Location(locationA.getWorld(), locationA.getX(), playerLocation.getY(), locationA.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
                    //player.sendMessage(CC.translate("&cYou can't move during the countdown!"));
                }
            } else if (player.equals(playerB)) {
                if (playerLocation.getBlockX() != locationB.getBlockX() || playerLocation.getBlockZ() != locationB.getBlockZ()) {
                    player.teleport(new Location(locationB.getWorld(), locationB.getX(), playerLocation.getY(), locationB.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
                    //player.sendMessage(CC.translate("&cYou can't move during the countdown!"));
                }
            }
        }
    }
}