package dev.revere.alley.game.bot.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.game.bot.BotFight;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 18/04/2025
 */
public class BotFightListener implements Listener {

    @EventHandler
    private void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity victimEntity = event.getEntity();
        Entity attackerEntity = event.getDamager();

        if (!(victimEntity instanceof Player) || !(attackerEntity instanceof Player)) {
            return;
        }

        Player victim = (Player) victimEntity;
        Player attacker = (Player) attackerEntity;

        boolean victimIsNPC = CitizensAPI.getNPCRegistry().isNPC(victim);
        boolean attackerIsNPC = CitizensAPI.getNPCRegistry().isNPC(attacker);

        if (!victimIsNPC && !attackerIsNPC) {
            return;
        }

        Player realPlayer = victimIsNPC ? attacker : victim;

        Profile profile = Alley.getInstance().getProfileService().getProfile(realPlayer.getUniqueId());
        if (profile == null || profile.getState() != EnumProfileState.FIGHTING_BOT) {
            return;
        }

        BotFight botFight = profile.getBotFight();
        if (botFight == null) {
            return;
        }

        NPC npc = botFight.getBot().getNpc();
        if (npc != null && npc.isSpawned()) {

        }
    }

    @EventHandler
    private void onDamage(NPCDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getNPC().getEntity();

        if (!(attacker instanceof Player) || !(victim instanceof Player)) {
            return;
        }

        Player realPlayer = (Player) attacker;

        Profile profile = Alley.getInstance().getProfileService().getProfile(realPlayer.getUniqueId());
        if (profile == null || profile.getState() != EnumProfileState.FIGHTING_BOT) {
            return;
        }

        BotFight botFight = profile.getBotFight();
        if (botFight == null) {
            return;
        }

        Entity botEntity = botFight.getBot().getNpc().getEntity();
        Player npcPlayer = (Player) botEntity;

        //TODO: :)
    }

    @EventHandler
    private void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != EnumProfileState.FIGHTING_BOT) {
            return;
        }

        BotFight botFight = profile.getBotFight();
        if (botFight != null) {
            botFight.handleDisconnect(player);
        }
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != EnumProfileState.FIGHTING_BOT) {
            return;
        }

        BotFight botFight = profile.getBotFight();
        if (botFight != null) {
            botFight.handleDisconnect(player);
        }
    }
}
