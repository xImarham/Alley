package me.emmy.alley.party.task;

import me.emmy.alley.Alley;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.party.PartyRequest;
import me.emmy.alley.util.chat.CC;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 03/11/2024 - 19:46
 */
public class PartyRequestExpiryTask extends BukkitRunnable {
    @Override
    public void run() {
        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();
        if (partyRepository.getParties().isEmpty()) {
            return;
        }

        List<PartyRequest> expiredRequests = new ArrayList<>();
        synchronized (partyRepository.getPartyRequests()) {
            partyRepository.getPartyRequests().removeIf(request -> {
                if (request.hasExpired()) {
                    expiredRequests.add(request);
                    return true;
                }
                return false;
            });

            this.notifyRequestIndividuals(expiredRequests);
        }
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