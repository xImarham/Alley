//package dev.revere.alley.feature.combat.task;
//
//import dev.revere.alley.Alley;
//import dev.revere.alley.feature.combat.CombatService;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//
///**
// * @author Emmy
// * @project Alley
// * @since 09/03/2025
// */
//public class CombatExpiryTask extends BukkitRunnable {
//    @Override
//    public void run() {
//        CombatService combatService = Alley.getInstance().getCombatService();
//        if (combatService.getCombatMap().isEmpty()) {
//            return;
//        }
//
//        combatService.getCombatMap().forEach((uuid, combat) -> {
//            Player player = Alley.getInstance().getServer().getPlayer(uuid);
//            if (combatService.isExpired(player)) {
//                combatService.removeLastAttacker(player, false);
//            }
//        });
//    }
//}