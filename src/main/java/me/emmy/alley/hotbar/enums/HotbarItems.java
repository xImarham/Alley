package me.emmy.alley.hotbar.enums;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public enum HotbarItems {

    QUEUES("&dQueues", Material.IRON_SWORD, 0, 0, "queues", HotbarType.LOBBY),
    CURRENT_MATCHES("&dCurrent Matches", Material.COMPASS, 0, 1, "currentmatches", HotbarType.LOBBY),
    KIT_EDITOR("&dKit Editor", Material.BOOK, 0, 2, "kiteditor", HotbarType.LOBBY, HotbarType.PARTY),
    PARTY("&dCreate Party", Material.NAME_TAG, 0, 4, "party create", HotbarType.LOBBY),
    LEADERBOARD("&dLeaderboard", Material.EMERALD, 0, 6, "leaderboards", HotbarType.LOBBY),
    EVENTS("&dEvents", Material.EYE_OF_ENDER, 0, 7, "host", HotbarType.LOBBY),
    SETTINGS("&dProfile Settings", Material.SKULL_ITEM, 0, 8, "profilemenu", HotbarType.LOBBY),

    DUO_UNRANKED_QUEUE("&dUnranked Duo Queue", Material.IRON_SWORD, 0, 0, "unrankedduo", HotbarType.PARTY),
    START_PARTY_EVENT("&dStart Party Event", Material.IRON_AXE, 0, 4, "party event", HotbarType.PARTY),
    FIGHT_OTHER_PARTY("&dFight Other Party", Material.DIAMOND_AXE, 0, 5, "party duel", HotbarType.PARTY),
    PARTY_INFO("&dParty Info", Material.PAPER, 0, 7, "party info", HotbarType.PARTY),
    PARTY_LEAVE("&cLeave Party", Material.REDSTONE, 0, 8, "party leave", HotbarType.PARTY),

    LEAVE_QUEUE("&cLeave Queue", Material.REDSTONE, 0, 8, "leavequeue", HotbarType.QUEUE),

    STOP_WATCHING("&cStop Watching", Material.REDSTONE, 0, 8, "leavespectator", HotbarType.SPECTATOR);

    ;

    private final Material material;
    private final Set<HotbarType> types;
    private final String command;
    private final String name;
    private final int data;
    private final int slot;

    /**
     * Constructor for the HotbarItems enum
     *
     * @param name     The name of the item
     * @param material The material of the item
     * @param data     The data of the item
     * @param slot     The slot of the item
     * @param command  The command of the item
     * @param types    The types of the item
     */
    HotbarItems(String name, Material material, int data, int slot, String command, HotbarType... types) {
        this.name = name;
        this.data = data;
        this.slot = slot;
        this.types = new HashSet<>();
        Collections.addAll(this.types, types);
        this.command = command;
        this.material = material;
    }
}
