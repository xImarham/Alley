package dev.revere.alley.feature.server;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.MatchRepository;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
@Getter
@Setter
public class ServerService {
    private boolean allowQueueing;

    public ServerService() {
        this.allowQueueing = true;
    }

    /**
     * Checks if queueing is enabled on the server, and if not, sends a message to the player.
     *
     * @param player the player to check.
     * @return true if the player is allowed to queue, false otherwise.
     */
    public boolean isQueueingEnabled(Player player) {
        if (!this.allowQueueing) {
            player.sendMessage(CC.translate("&cThe server is currently preparing for a reboot. You cannot queue at this time."));
            return true;
        }

        return false;
    }

    /**
     * Disband all matches on the server.
     *
     * @param player the player that clicked the button
     * @param plugin the instance of the plugin
     */
    public void disbandMatches(Player player, Alley plugin) {
        MatchRepository matchRepository = plugin.getMatchRepository();
        List<AbstractMatch> matches = new ArrayList<>(matchRepository.getMatches());
        if (matches.isEmpty()) {
            player.sendMessage(CC.translate("&cCould not find any matches to end."));
        } else {
            int rankedMatches = 0;
            int unrankedMatches = 0;

            for (AbstractMatch match : matches) {
                if (match.isRanked()) {
                    rankedMatches++;
                } else {
                    unrankedMatches++;
                }
                match.endMatch();
            }

            player.sendMessage(CC.translate("&cEnding a total of &f" + unrankedMatches + " &cunranked matches and &f" + rankedMatches + " &cranked matches."));
        }
    }

    /**
     * Disband all parties on the server.
     *
     * @param player the player that clicked the button
     * @param plugin the instance of the plugin
     */
    public void disbandParties(Player player, Alley plugin) {
        PartyService partyService = plugin.getPartyService();
        List<Party> parties = new ArrayList<>(partyService.getParties());

        if (parties.isEmpty()) {
            player.sendMessage(CC.translate("&cCould not find any parties to disband."));
        } else {
            player.sendMessage(CC.translate("&cDisbanding a total of &f" + parties.size() + " &cparties."));

            for (Party party : parties) {
                partyService.disbandParty(party.getLeader());
            }
        }
    }

    /**
     * Remove all players from the queue.
     *
     * @param player the player that clicked the button
     * @param plugin the instance of the plugin
     */
    public void removePlayersFromQueue(Player player, Alley plugin) {
        for (Profile profile : plugin.getProfileService().getProfiles().values()) {
            if (profile.getState() == EnumProfileState.WAITING) {
                Player queuePlayer = plugin.getServer().getPlayer(profile.getUuid());
                profile.setState(EnumProfileState.LOBBY);
                plugin.getQueueService().getQueues().remove(profile.getQueueProfile().getQueue());
                profile.setQueueProfile(null);
                plugin.getHotbarService().applyHotbarItems(queuePlayer, EnumHotbarType.LOBBY);
                plugin.getSpawnService().teleportToSpawn(queuePlayer);
                queuePlayer.sendMessage(CC.translate("&cYou've been removed from the queue due to a server reboot."));
            }
        }

        player.sendMessage(CC.translate("&cRemoved all queueing players from the queue."));
    }
}