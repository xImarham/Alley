package dev.revere.alley.task;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.game.duel.DuelRequestHandler;
import dev.revere.alley.game.duel.DuelRequest;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.game.party.PartyRequest;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class RepositoryCleanupTask extends BukkitRunnable {

    @Override
    public void run() {
        CombatService combatService = Alley.getInstance().getCombatService();
        if (!combatService.getCombatMap().isEmpty()) {
            combatService.getCombatMap().forEach((uuid, combat) -> {
                Player player = Alley.getInstance().getServer().getPlayer(uuid);
                if (combatService.isExpired(player)) {
                    combatService.removeLastAttacker(player, false);
                }
            });
        }

        DuelRequestHandler duelRequestHandler = Alley.getInstance().getDuelRequestHandler();
        if (!duelRequestHandler.getDuelRequests().isEmpty()) {
            List<DuelRequest> expiredRequests = new ArrayList<>();
            synchronized (duelRequestHandler.getDuelRequests()) {
                duelRequestHandler.getDuelRequests().removeIf(duelRequest -> {
                    if (duelRequest.hasExpired()) {
                        expiredRequests.add(duelRequest);
                        return true;
                    }
                    return false;
                });
            }
            this.notifyDuelRequestIndividuals(expiredRequests);
        }

        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
        if (!partyHandler.getParties().isEmpty()) {
            List<PartyRequest> expiredRequests = new ArrayList<>();
            synchronized (partyHandler.getPartyRequests()) {
                partyHandler.getPartyRequests().removeIf(request -> {
                    if (request.hasExpired()) {
                        expiredRequests.add(request);
                        return true;
                    }
                    return false;
                });
            }
            this.notifyPartyRequestIndividuals(expiredRequests);
        }
    }

    /**
     * Notify the sender and target that the duel request has expired.
     *
     * @param expiredRequests the expired requests
     */
    private void notifyDuelRequestIndividuals(List<DuelRequest> expiredRequests) {
        expiredRequests.forEach(duelRequest -> {
            duelRequest.getSender().sendMessage(CC.translate("&cYour duel request to " + duelRequest.getTarget().getName() + " has expired."));
            duelRequest.getTarget().sendMessage(CC.translate("&cThe duel request from " + duelRequest.getSender().getName() + " has expired."));
        });
    }

    /**
     * Notifies the individuals that their party request has expired.
     *
     * @param partyRequests The party requests that have expired.
     */
    private void notifyPartyRequestIndividuals(List<PartyRequest> partyRequests) {
        partyRequests.forEach(partyRequest -> {
            partyRequest.getSender().sendMessage(CC.translate("&cYour party request to &b" + partyRequest.getSender().getName() + " &chas expired."));
            partyRequest.getTarget().sendMessage(CC.translate("&cThe party request from &b" + partyRequest.getTarget().getName() + " &chas expired."));
        });
    }
}