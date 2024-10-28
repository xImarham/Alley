package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.chat.CC;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 21:45
 */
@Getter
@Setter
public class PartyRepository {
    private final List<Party> parties = new ArrayList<>();
    private final List<PartyRequest> partyRequests = new ArrayList<>();

    /**
     * Gets the party of a leader.
     *
     * @param player The leader of the party.
     * @return The party of the leader.
     */
    public Party getPartyByLeader(Player player) {
        return parties.stream()
                .filter(party -> party.getLeader().equals(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the party of a member.
     *
     * @param uuid The member's UUID.
     * @return The party of the member.
     */
    public Party getPartyByMember(UUID uuid) {
        return parties.stream()
                .filter(party -> party.getMembers().contains(uuid))
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a party.
     *
     * @param player The leader of the party.
     */
    public void createParty(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot create a party in this state."));
            return;
        }
        Party party = new Party(player);
        profile.setParty(party);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.PARTY);
    }

    /**
     * Disbands the party.
     *
     * @param leader The leader of the party.
     */
    public void disbandParty(Player leader) {
        Party party = getPartyByLeader(leader);
        if (party != null) {
            party.disbandParty();
        }
    }

    /**
     * Joins a party.
     *
     * @param player The player to join the party.
     * @param leader The leader of the party.
     */
    public void joinParty(Player player, Player leader) {
        Party party = getPartyByLeader(leader);
        if (party != null) {
            party.joinParty(player);
        }
    }

    /**
     * Removes a member from the party.
     *
     * @param player The member to remove.
     */
    public void leaveParty(Player player) {
        Party party = getPartyByMember(player.getUniqueId());
        if (party != null) {
            party.leaveParty(player);
        }
    }

    /**
     * Kicks a member from the party.
     *
     * @param leader The leader of the party.
     * @param member The member to kick.
     */
    public void kickMember(Player leader, Player member) {
        Party party = getPartyByLeader(leader);
        if (party != null && party.getMembers().contains(member.getUniqueId())) {
            party.leaveParty(member);
        }
    }

    /**
     * Adds a party request to the repository.
     *
     * @param request The party request to add.
     */
    public void addRequest(PartyRequest request) {
        partyRequests.add(request);
    }

    /**
     * Gets a party request for a specific player.
     *
     * @param player The player to get the request for.
     * @return The party request for the player.
     */
    public PartyRequest getRequest(Player player) {
        return partyRequests.stream()
                .filter(request -> request.getTarget().equals(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a party request from the repository.
     *
     * @param request The party request to remove.
     */
    public void removeRequest(PartyRequest request) {
        partyRequests.remove(request);
    }

    /**
     * Sends a party request to the target player.
     *
     * @param party  The party to send the request to.
     * @param target The target player to send the request to.
     */
    public void sendRequest(Party party, Player target) {
        party.notifyParty("&b" + target.getName() + " &ahas been invited to the party.");

        String partyLeader = party.getLeader().getName();

        TextComponent invitation = new TextComponent(CC.translate("&b" + partyLeader + " &ahas invited you to their party! [CLICK TO JOIN]"));
        invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + partyLeader));

        String hover = CC.translate("&aClick to accept " + partyLeader + "&a's party invitation.");
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();
        invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));

        target.spigot().sendMessage(invitation);
    }

    /**
     * Expires party requests asynchronously.
     */
    public void expirePartyInvitesAsynchronously() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (partyRequests.isEmpty()) return;

                List<PartyRequest> expiredRequests = new ArrayList<>();
                synchronized (partyRequests) {
                    partyRequests.removeIf(request -> {
                        if (request.hasExpired()) {
                            expiredRequests.add(request);
                            return true;
                        }
                        return false;
                    });

                    notifyRequestIndividuals(expiredRequests);
                }
            }
        }.runTaskTimerAsynchronously(Alley.getInstance(), 40L, 40L);
    }

    /**
     * Notifies the individuals that their party request has expired.
     *
     * @param partyRequests The party requests that have expired.
     */
    private void notifyRequestIndividuals(List<PartyRequest> partyRequests) {
        partyRequests.forEach(partyRequest -> {
            partyRequest.getSender().sendMessage(CC.translate("&cYour party request to &b" + partyRequest.getSender().getName() + " &chas expired."));
            partyRequest.getTarget().sendMessage(CC.translate("&cThe party request from &b" + partyRequest.getTarget().getName() + " &chas expired."));
        });
    }
}