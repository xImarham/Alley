package me.emmy.alley.ffa.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.ffa.enums.EnumFFAState;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 14:24
 */

public class FFAListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setDeathMessage(null);
            event.getDrops().clear();
            profile.getFfaGame().setFfaState(EnumFFAState.SPAWN);
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
            return;
        }
    }
}
