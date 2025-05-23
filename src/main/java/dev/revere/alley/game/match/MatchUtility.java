package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
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
     * @param match        the match
     */
    public void denyPlayerMovement(List<GameParticipant<MatchGamePlayerImpl>> participants, AbstractMatch match) {
        if (participants.size() == 2) {
            GameParticipant<?> participantA = participants.get(0);
            GameParticipant<?> participantB = participants.get(1);

            Location locationA = match.getArena().getPos1();
            Location locationB = match.getArena().getPos2();

            for (GamePlayer gamePlayer : participantA.getPlayers()) {
                Player participantPlayer = gamePlayer.getPlayer();
                if (participantPlayer != null) {
                    teleportBackIfMoved(participantPlayer, locationA);
                }
            }

            for (GamePlayer gamePlayer : participantB.getPlayers()) {
                Player participantPlayer = gamePlayer.getPlayer();
                if (participantPlayer != null) {
                    teleportBackIfMoved(participantPlayer, locationB);
                }
            }
        }
    }

    /**
     * Teleports the player back to their designated position if they moved.
     *
     * @param player   The player to check.
     * @param location The designated location.
     */
    private void teleportBackIfMoved(Player player, Location location) {
        Location playerLocation = player.getLocation();

        if (playerLocation.getBlockX() != location.getBlockX() || playerLocation.getBlockZ() != location.getBlockZ()) {
            player.teleport(new Location(location.getWorld(), location.getX(), playerLocation.getY(), location.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
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
        AbstractArena arena = profile.getMatch().getArena();
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
    /**
     * Sends the match result message.
     *
     * @param match      The match.
     * @param winnerName The name of the winner.
     * @param loserName  The name of the loser.
     */
    public void sendMatchResult(AbstractMatch match, String winnerName, String loserName) {
        FileConfiguration config = Alley.getInstance().getConfigService().getMessagesConfig();

        String winnerCommand = config.getString("match.ended.match-result.winner.command").replace("{winner}", winnerName);
        String winnerHover = config.getString("match.ended.match-result.winner.hover").replace("{winner}", winnerName);
        String loserCommand = config.getString("match.ended.match-result.loser.command").replace("{loser}", loserName);
        String loserHover = config.getString("match.ended.match-result.loser.hover").replace("{loser}", loserName);

        for (String line : Alley.getInstance().getConfigService().getMessagesConfig().getStringList("match.ended.match-result.format")) {
            if (line.contains("{winner}") && line.contains("{loser}")) {
                String[] parts = line.split("\\{winner}", 2);

                if (parts.length > 1) {
                    String[] loserParts = parts[1].split("\\{loser}", 2);

                    TextComponent winnerComponent = new TextComponent(CC.translate(winnerName));
                    winnerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, winnerCommand));
                    winnerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(winnerHover)).create()));

                    TextComponent loserComponent = new TextComponent(CC.translate(loserName));
                    loserComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, loserCommand));
                    loserComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(loserHover)).create()));

                    match.sendCombinedSpigotMessage(
                            new TextComponent(CC.translate(parts[0])),
                            winnerComponent,
                            new TextComponent(CC.translate(loserParts[0])),
                            loserComponent,
                            new TextComponent(loserParts.length > 1 ? CC.translate(loserParts[1]) : "")
                    );
                }
            } else if (line.contains("{winner}")) {
                String[] parts = line.split("\\{winner}", 2);

                TextComponent winnerComponent = new TextComponent(CC.translate(winnerName));
                winnerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, winnerCommand));
                winnerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(winnerHover)).create()));

                match.sendCombinedSpigotMessage(
                        new TextComponent(CC.translate(parts[0])),
                        winnerComponent,
                        new TextComponent(parts.length > 1 ? CC.translate(parts[1]) : "")
                );
            } else if (line.contains("{loser}")) {
                String[] parts = line.split("\\{loser}", 2);

                TextComponent loserComponent = new TextComponent(CC.translate(loserName));
                loserComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, loserCommand));
                loserComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(loserHover)).create()));

                match.sendCombinedSpigotMessage(
                        new TextComponent(CC.translate(parts[0])),
                        loserComponent,
                        new TextComponent(parts.length > 1 ? CC.translate(parts[1]) : "")
                );
            } else {
                match.sendMessage(CC.translate(line));
            }
        }
    }
}