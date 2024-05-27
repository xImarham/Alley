package me.emmy.alley.ffa.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.ffa.enums.EnumFFAState;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.TaskUtil;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class DefaultFFAMatchImpl extends AbstractFFAMatch {
    /**
     * Constructor for the DefaultFFAMatchImpl class
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public DefaultFFAMatchImpl(String name, Arena arena, Kit kit, int maxPlayers) {
        super(name, arena, kit, maxPlayers);
    }

    @Override
    public void join(Player player) {
        if (getPlayers().size() >= getMaxPlayers()) {
            player.sendMessage(CC.translate("&cThis FFA match is full. " + getMaxPlayers() + " players are already in the match."));
            return;
        }

        getPlayers().add(player);
        getPlayers().forEach(online -> online.sendMessage(CC.translate("&a" + player.getName() + " has joined the FFA match.")));
        setupPlayer(player);
    }

    @Override
    public void leave(Player player) {
        getPlayers().remove(player);
        getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has left the FFA match.")));

        player.sendMessage(CC.translate("&aYou have left the FFA match."));

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setFfaMatch(null);

        PlayerUtil.reset(player);
        Alley.getInstance().getSpawnHandler().teleportToSpawn(player);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
    }

    @Override
    public void setupPlayer(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        Arena arena = getArena();
        player.teleport(arena.getPos1());

        Kit kit = getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(kit.getInventory());
    }

    @Override
    public void handleDeath(Player player, Player killer) {
        if (killer == null) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            profile.getProfileData().getFfaData().get(getKit().getName()).incrementDeaths();

            getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has died.")));
            setupPlayer(player);
            return;
        }

        Profile killerProfile = Alley.getInstance().getProfileRepository().getProfile(killer.getUniqueId());
        if (killerProfile.getProfileData().getFfaData().get(getKit().getName()) != null) {
            killerProfile.getProfileData().getFfaData().get(getKit().getName()).incrementKills();
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.getProfileData().getFfaData().get(getKit().getName()).incrementDeaths();

        getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has been killed by " + killer.getName() + ".")));

        Arena arena = getArena();
        player.teleport(arena.getPos1());

        Kit kit = getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(kit.getInventory());
    }
}
