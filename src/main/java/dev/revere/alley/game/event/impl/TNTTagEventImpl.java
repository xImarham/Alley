package dev.revere.alley.game.event.impl;

import dev.revere.alley.game.event.Event;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 12/10/2024 - 22:51
 */
public class TNTTagEventImpl implements Event {
    @Override
    public String name() {
        return CC.translate("&b&lTNT Tag");
    }

    @Override
    public String description() {
        return CC.translate("&7Don't get tagged!");
    }

    @Override
    public Material icon() {
        return Material.TNT;
    }

    @Override
    public int durability() {
        return 0;
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public void handleDeath(Player player) {

    }

    @Override
    public void respawnPlayer(Player player) {

    }

    @Override
    public void preparePlayer(Player player) {

    }

    @Override
    public void denyMovement(Player player) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void end() {

    }

    @Override
    public void broadcast(String message, List<Player> players) {

    }
}