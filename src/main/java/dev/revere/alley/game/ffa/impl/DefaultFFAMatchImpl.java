package dev.revere.alley.game.ffa.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.ffa.enums.EnumFFAState;
import dev.revere.alley.game.ffa.player.GameFFAPlayer;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class DefaultFFAMatchImpl extends AbstractFFAMatch {
    protected final Alley plugin = Alley.getInstance();

    /**
     * Constructor for the DefaultFFAMatchImpl class.
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public DefaultFFAMatchImpl(String name, AbstractArena arena, Kit kit, int maxPlayers) {
        super(name, arena, kit, maxPlayers);
    }

    /**
     * Join a player to the FFA match.
     *
     * @param player The player
     */
    @Override
    public void join(Player player) {
        GameFFAPlayer gameFFAPlayer = new GameFFAPlayer(player.getUniqueId(), player.getName());
        if (this.getPlayers().size() >= this.getMaxPlayers()) {
            player.sendMessage(CC.translate("&cThis FFA match is full. " + getMaxPlayers() + " players are already in the match."));
            return;
        }

        this.getPlayers().add(gameFFAPlayer);
        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&a" + player.getName() + " has joined the FFA match.")));
        this.setupPlayer(player);
    }

    /**
     * Force a player to join the FFA match.
     *
     * @param player The player
     */
    public void forceJoin(Player player) {
        GameFFAPlayer gameFFAPlayer = new GameFFAPlayer(player.getUniqueId(), player.getName());
        this.getPlayers().add(gameFFAPlayer);
        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&a" + player.getName() + " has been forced into the FFA match.")));
        this.setupPlayer(player);
    }

    /**
     * Leave a player from the FFA match.
     *
     * @param player The player
     */
    @Override
    public void leave(Player player) {
        GameFFAPlayer gameFFAPlayer = this.getGameFFAPlayer(player);
        this.getPlayers().remove(gameFFAPlayer);

        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&c" + player.getName() + " has left the FFA match.")));

        player.sendMessage(CC.translate("&aYou have left the FFA match."));

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setFfaMatch(null);

        this.plugin.getVisibilityService().updateVisibility(player);

        PlayerUtil.reset(player, false);
        this.plugin.getSpawnService().teleportToSpawn(player);
        this.plugin.getHotbarService().applyHotbarItems(player);
    }

    /**
     * Setup a player for the FFA match.
     *
     * @param player The player
     */
    @Override
    public void setupPlayer(Player player) {
        GameFFAPlayer gameFFAPlayer = this.getGameFFAPlayer(player);
        gameFFAPlayer.setState(EnumFFAState.SPAWN);

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        this.plugin.getVisibilityService().updateVisibility(player);
        this.plugin.getKnockbackAdapter().getKnockbackType().applyKnockback(player.getPlayer(), getKit().getKnockbackProfile());

        PlayerUtil.reset(player, true);

        AbstractArena arena = this.getArena();
        player.teleport(arena.getPos1());

        Kit kit = this.getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(kit.getItems());
    }

    /**
     * Handle the respawn of a player.
     *
     * @param player The player
     */
    public void handleRespawn(Player player) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        AbstractArena arena = this.getArena();

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            player.teleport(arena.getPos1());

            Kit kit = this.getKit();
            player.getInventory().clear();
            player.getInventory().setArmorContents(kit.getArmor());
            player.getInventory().setContents(kit.getItems());
            player.updateInventory();
        }, 1L);

        GameFFAPlayer gameFFAPlayer = this.getGameFFAPlayer(player);
        gameFFAPlayer.setState(EnumFFAState.SPAWN);
    }

    /**
     * Handle the death of a player.
     *
     * @param player The player
     * @param killer The killer
     */
    @Override
    public void handleDeath(Player player, Player killer) {
        if (killer == null) {
            Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
            profile.getProfileData().getFfaData().get(this.getKit().getName()).incrementDeaths();
            this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&c" + player.getName() + " has died.")));
            this.handleRespawn(player);
            return;
        }

        Profile killerProfile = this.plugin.getProfileService().getProfile(killer.getUniqueId());
        if (killerProfile.getProfileData().getFfaData().get(getKit().getName()) != null) {
            killerProfile.getProfileData().getFfaData().get(getKit().getName()).incrementKills();
        }

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.getProfileData().getFfaData().get(getKit().getName()).incrementDeaths();

        this.plugin.getReflectionRepository().getReflectionService(ActionBarReflectionService.class).sendDeathMessage(killer, player);
        this.plugin.getCombatService().resetCombatLog(player);

        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&6" + player.getName() + " &ahas been killed by &6" + killer.getName() + "&a.")));
        this.handleRespawn(player);
    }
}