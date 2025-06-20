package dev.revere.alley.game.ffa;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.ffa.player.GameFFAPlayer;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public abstract class AbstractFFAMatch {
    protected final Alley plugin = Alley.getInstance();

    private final String name;

    private final AbstractArena arena;
    private final Kit kit;

    private int maxPlayers;

    private List<GameFFAPlayer> players;
    private List<UUID> spectators;

    /**
     * Constructor for the AbstractFFAMatch class.
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public AbstractFFAMatch(String name, AbstractArena arena, Kit kit, int maxPlayers) {
        this.name = name;
        this.arena = arena;
        this.kit = kit;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
    }

    public abstract void join(Player player);

    public abstract void setupPlayer(Player player);

    public abstract void handleDeath(Player player, Player killer);

    public abstract void handleRespawn(Player player);

    public abstract void leave(Player player);


    /**
     * Method to spectate the match as a player.
     *
     * @param player The player who wants to spectate the match.
     */
    public void addSpectator(Player player) {
        ProfileService profileService = this.plugin.getProfileService();
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (this.arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        if (profile.getState() != EnumProfileState.SPECTATING) {
            profile.setState(EnumProfileState.SPECTATING);
            profile.setFfaMatch(this);

            this.plugin.getVisibilityService().updateVisibility(player);
            this.plugin.getHotbarService().applyHotbarItems(player, EnumHotbarType.SPECTATOR);
        }

        player.teleport(this.arena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        this.spectators.add(player.getUniqueId());
    }

    /**
     * Method to remove a player from the spectator list and reset their state.
     *
     * @param player The player to remove from the spectator list.
     */
    public void removeSpectator(Player player) {
        ProfileService profileService = this.plugin.getProfileService();
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.SPECTATING) {
            profile.setState(EnumProfileState.LOBBY);
            profile.setFfaMatch(null);

            this.plugin.getVisibilityService().updateVisibility(player);
            this.plugin.getHotbarService().applyHotbarItems(player, EnumHotbarType.LOBBY);
        }

        player.spigot().setCollidesWithEntities(true);
        player.setAllowFlight(false);
        player.setFlying(false);

        this.plugin.getSpawnService().teleportToSpawn(player);

        this.spectators.remove(player.getUniqueId());
    }

    /**
     * Method to get an instance of GameFFAPlayer from a Player object.
     *
     * @param player The Player object to get the GameFFAPlayer from.
     * @return The GameFFAPlayer instance associated with the Player, or null if not found.
     */
    public GameFFAPlayer getGameFFAPlayer(Player player) {
        return this.players.stream()
                .filter(ffaPlayer -> ffaPlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Handle the combat log of a player and killer, updating stats of the combat logger, ect...
     *
     * @param player The player
     * @param killer The killer
     */
    public void handleCombatLog(Player player, Player killer) {
        ProfileService profileService = this.plugin.getProfileService();
        Profile profile = profileService.getProfile(player.getUniqueId());
        Profile killerProfile = profileService.getProfile(killer.getUniqueId());

        profile.getProfileData().getFfaData().get(this.getKit().getName()).incrementDeaths();
        killerProfile.getProfileData().getFfaData().get(this.getKit().getName()).incrementKills();

        this.getPlayers().forEach(ffaPlayer -> ffaPlayer.getPlayer().sendMessage(CC.translate("&7(Combat Log) &c" + player.getName() + " has been killed by " + killer.getName() + ".")));

        CombatService combatService = this.plugin.getCombatService();
        combatService.resetCombatLog(player);
    }
}