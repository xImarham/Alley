//package dev.revere.alley.game.party.task;
//
//import dev.revere.alley.Alley;
//import dev.revere.alley.game.party.PartyHandler;
//import dev.revere.alley.game.party.PartyRequest;
//import dev.revere.alley.util.chat.CC;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Emmy
// * @project Alley
// * @date 03/11/2024 - 19:46
// */
//public class PartyRequestExpiryTask extends BukkitRunnable {
//    @Override
//    public void run() {
//        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
//        if (partyHandler.getParties().isEmpty()) {
//            return;
//        }
//
//        List<PartyRequest> expiredRequests = new ArrayList<>();
//        synchronized (partyHandler.getPartyRequests()) {
//            partyHandler.getPartyRequests().removeIf(request -> {
//                if (request.hasExpired()) {
//                    expiredRequests.add(request);
//                    return true;
//                }
//                return false;
//            });
//
//            this.notifyRequestIndividuals(expiredRequests);
//        }
//    }
//
//    /**
//     * Notifies the individuals that their party request has expired.
//     *
//     * @param partyRequests The party requests that have expired.
//     */
//    private void notifyRequestIndividuals(List<PartyRequest> partyRequests) {
//        partyRequests.forEach(partyRequest -> {
//            partyRequest.getSender().sendMessage(CC.translate("&cYour party request to &b" + partyRequest.getSender().getName() + " &chas expired."));
//            partyRequest.getTarget().sendMessage(CC.translate("&cThe party request from &b" + partyRequest.getTarget().getName() + " &chas expired."));
//        });
//    }
//}