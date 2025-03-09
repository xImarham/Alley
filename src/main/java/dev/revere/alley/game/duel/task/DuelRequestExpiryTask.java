//package dev.revere.alley.game.duel.task;
//
//import dev.revere.alley.Alley;
//import dev.revere.alley.game.duel.DuelRequestHandler;
//import dev.revere.alley.game.duel.DuelRequest;
//import dev.revere.alley.util.chat.CC;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Emmy
// * @project Alley
// * @date 03/11/2024 - 19:58
// */
//public class DuelRequestExpiryTask extends BukkitRunnable {
//    @Override
//    public void run() {
//        DuelRequestHandler duelRequestHandler = Alley.getInstance().getDuelRequestHandler();
//        if (duelRequestHandler.getDuelRequests().isEmpty()) {
//            return;
//        }
//
//        List<DuelRequest> expiredRequests = new ArrayList<>();
//        synchronized (duelRequestHandler.getDuelRequests()) {
//            duelRequestHandler.getDuelRequests().removeIf(duelRequest -> {
//                if (duelRequest.hasExpired()) {
//                    expiredRequests.add(duelRequest);
//                    return true;
//                }
//                return false;
//            });
//        }
//
//        this.notifyRequestIndividuals(expiredRequests);
//    }
//
//    /**
//     * Notify the sender and target that the duel request has expired.
//     *
//     * @param expiredRequests the expired requests
//     */
//    private void notifyRequestIndividuals(List<DuelRequest> expiredRequests) {
//        expiredRequests.forEach(duelRequest -> {
//            duelRequest.getSender().sendMessage(CC.translate("&cYour duel request to " + duelRequest.getTarget().getName() + " has expired."));
//            duelRequest.getTarget().sendMessage(CC.translate("&cThe duel request from " + duelRequest.getSender().getName() + " has expired."));
//        });
//    }
//}
