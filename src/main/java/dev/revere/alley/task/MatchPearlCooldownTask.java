package dev.revere.alley.task;

import dev.revere.alley.Alley;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

/**
 * @author Remi
 * @project alley-practice
 * @date 26/06/2025
 */
public class MatchPearlCooldownTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());

            if (profile.getState() == EnumProfileState.PLAYING || profile.getState() == EnumProfileState.FFA) {
                CooldownRepository cooldownRepository = Alley.getInstance().getCooldownRepository();
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), EnumCooldownType.ENDER_PEARL));

                if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {

                    Cooldown cooldown = optionalCooldown.get();

                    long remainingMillis = cooldown.remainingTimeMillis();
                    long totalDuration = cooldown.getType().getCooldownDuration();
                    int remainingTime = optionalCooldown.get().remainingTime();

                    player.setLevel(remainingTime);

                    if (totalDuration > 0) {
                        player.setExp((float) remainingMillis / totalDuration);
                    }
                }
            } else {
                if (player.getLevel() > 0) {
                    player.setLevel(0);
                }

                if (player.getExp() > 0.0F) {
                    player.setExp(0.0F);
                }
            }
        }
    }
}
