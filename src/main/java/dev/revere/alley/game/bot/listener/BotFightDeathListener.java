package dev.revere.alley.game.bot.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.game.bot.BotFight;
import dev.revere.alley.game.bot.enums.EnumBotFightState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import net.citizensnpcs.api.event.NPCDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 18/04/2025
 */
public class BotFightDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());

        if (profile == null || profile.getState() != EnumProfileState.FIGHTING_BOT) {
            return;
        }

        Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> player.spigot().respawn(), 1L);

        BotFight botFight = profile.getBotFight();
        if (botFight != null) {
            botFight.handleDeath(player);
            botFight.setState(EnumBotFightState.ENDING);
            botFight.getRunnable().setStage(4);
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FIGHTING_BOT) {
            event.setRespawnLocation(player.getLocation());
        }
    }

    @EventHandler
    private void onNPCDeath(NPCDeathEvent event) {
        if (!event.getNPC().isSpawned()) return;

        //TODO: Doesn't work, will need to fix this or just try a different approach i don't like this bot fight system either way

        for (BotFight botFight : Alley.getInstance().getBotFightRepository().getBotFights()) {
            if (botFight.getBot().getNpc().getId() == event.getNPC().getId()) {
                Player player = botFight.getPlayer();
                if (player != null) {
                    botFight.handleDeath(player);
                    botFight.setState(EnumBotFightState.ENDING);
                    botFight.getRunnable().setStage(4);
                    botFight.sendMessage("The bot has been defeated! The fight is ending.");
                    return;
                }
            }
        }
    }
}