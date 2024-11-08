package dev.revere.alley.game.match;

import dev.revere.alley.arena.Arena;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.profile.Profile;
import lombok.experimental.UtilityClass;
import dev.revere.alley.game.match.player.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
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
            Location locationA = match.getArena().getPos1();
            Location locationB = match.getArena().getPos2();

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

    /**
     * Check if a location is beyond the bounds of an arena.
     *
     * @param location      the location
     * @param profile the profile
     * @return if the location is beyond the bounds
     */
    public boolean isBeyondBounds(Location location, Profile profile) {
        Arena arena = profile.getMatch().getArena();
        Location corner1 = arena.getMinimum();
        Location corner2 = arena.getMaximum();

        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        boolean withinBounds;
        if (profile.getMatch().getState() == EnumMatchState.ENDING_MATCH) {
            withinBounds = location.getX() >= minX && location.getX() <= maxX && location.getZ() >= minZ && location.getZ() <= maxZ;
        } else {
            withinBounds = location.getX() >= minX && location.getX() <= maxX && location.getY() >= minY && location.getY() <= maxY && location.getZ() >= minZ && location.getZ() <= maxZ;
        }

        return !withinBounds;
    }
}