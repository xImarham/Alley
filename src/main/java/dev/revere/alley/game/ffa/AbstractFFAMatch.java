package dev.revere.alley.game.ffa;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileRepository;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.game.ffa.enums.EnumFFAState;
import dev.revere.alley.feature.kit.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public abstract class AbstractFFAMatch {
    private EnumFFAState state = EnumFFAState.SPAWN;

    private final String name;
    private final AbstractArena arena;
    private final Kit kit;
    private int maxPlayers;
    private List<Player> players;

    /**
     * Constructor for the AbstractFFAMatch class.
     *
     * @param name The name of the match
     * @param arena The arena the match is being played in
     * @param kit The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public AbstractFFAMatch(String name, AbstractArena arena, Kit kit, int maxPlayers) {
        this.name = name;
        this.arena = arena;
        this.kit = kit;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
    }

    public abstract void join(Player player);
    public abstract void setupPlayer(Player player);
    public abstract void handleDeath(Player player, Player killer);
    public abstract void handleRespawn(Player player);
    public abstract void leave(Player player);

    /**
     * Handle the combat log of a player and killer, updating stats of the combat logger, ect...
     *
     * @param player The player
     * @param killer The killer
     */
    public void handleCombatLog(Player player, Player killer) {
        ProfileRepository profileRepository = Alley.getInstance().getProfileRepository();
        Profile profile = profileRepository.getProfile(player.getUniqueId());
        Profile killerProfile = profileRepository.getProfile(killer.getUniqueId());

        profile.getProfileData().getFfaData().get(this.getKit().getName()).incrementDeaths();
        killerProfile.getProfileData().getFfaData().get(this.getKit().getName()).incrementKills();

        this.getPlayers().forEach(online -> online.sendMessage(CC.translate("&7(Combat Log) &c" + player.getName() + " has been killed by " + killer.getName() + ".")));

        CombatService combatService = Alley.getInstance().getCombatService();
        combatService.resetCombatLog(player);
    }
}