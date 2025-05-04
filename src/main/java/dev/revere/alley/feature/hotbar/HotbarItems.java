package dev.revere.alley.feature.hotbar;

import dev.revere.alley.feature.hotbar.enums.EnumHotbarType;
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

    // Example of how you can have one hotbar item in 2 specific states:
    // EXAMPLE_ITEM("&bEmmy is smart", Material.ZIUES_SKULL, 0, 2, "alley", HotbarType.LOBBY, HotbarType.QUEUE)

    UNRANKED_QUEUES("&bUnranked Queues &7(Right Click)", Material.IRON_SWORD, 0, 0, "", EnumHotbarType.LOBBY),
    RANKED_QUEUES("&bRanked Queues &7(Right Click)", Material.DIAMOND_SWORD, 0, 1, "", EnumHotbarType.LOBBY),
    KIT_EDITOR("&bKit Editor &7(Right Click)", Material.BOOK, 0, 2, "kiteditor", EnumHotbarType.LOBBY),
    CURRENT_MATCHES("&bCurrent Matches &7(Right Click)", Material.COMPASS, 0, 3, "currentmatches", EnumHotbarType.LOBBY),
    PARTY("&bCreate Party &7(Right Click)", Material.NAME_TAG, 0, 5, "party create", EnumHotbarType.LOBBY),
    LEADERBOARD("&bLeaderboards &7(Right Click)", Material.EMERALD, 0, 6, "leaderboards", EnumHotbarType.LOBBY),
    EVENTS("&bEvents &7(Right Click)", Material.EYE_OF_ENDER, 0, 7, "host", EnumHotbarType.LOBBY),
    SETTINGS("&bSettings &7(Right Click)", Material.SKULL_ITEM, 0, 8, "practicesettings", EnumHotbarType.LOBBY),

    DUO_UNRANKED_QUEUE("&bUnranked Duo Queue &7(Right Click)", Material.IRON_SWORD, 0, 0, "unrankedduo", EnumHotbarType.PARTY),
    KIT_EDITOR_PARTY("&bKit Editor &7(Right Click)", Material.BOOK, 0, 1, "kiteditor", EnumHotbarType.PARTY),
    START_PARTY_EVENT("&bStart Party Event &7(Right Click)", Material.IRON_AXE, 0, 4, "", EnumHotbarType.PARTY),
    FIGHT_OTHER_PARTY("&bFight Other Party &7(Right Click)", Material.DIAMOND_AXE, 0, 5, "", EnumHotbarType.PARTY),
    PARTY_INFO("&bParty Info &7(Right Click)", Material.PAPER, 0, 7, "party info", EnumHotbarType.PARTY),
    PARTY_LEAVE("&cLeave Party &7(Right Click)", Material.REDSTONE, 0, 8, "party leave", EnumHotbarType.PARTY),

    //Yes, two times kiteditor, because i want it to be in the first slot and i cant be bothered to make the necessary changes
    KIT_EDITOR_2("&bKit Editor &7(Right Click)", Material.BOOK, 0, 0, "kiteditor", EnumHotbarType.QUEUE),
    TIPS("&bTip Of The Day &7(Right Click)", Material.PAPER, 0, 1, "tip", EnumHotbarType.QUEUE),
    LEAVE_QUEUE("&cLeave Queue &7(Right Click)", Material.REDSTONE, 0, 8, "leavequeue", EnumHotbarType.QUEUE),

    STOP_WATCHING("&cStop Watching &7(Right Click)", Material.REDSTONE, 0, 8, "leavespectator", EnumHotbarType.SPECTATOR),

    TOURNAMENT_LEAVE("&cLeave Tournament &7(Right Click)", Material.REDSTONE, 0, 8, "tournament leave", EnumHotbarType.TOURNAMENT),
    TOURNAMENT_INFO("&bTournament Info &7(Right Click)", Material.PAPER, 0, 0, "tournament info", EnumHotbarType.TOURNAMENT),

    ;

    private final Material material;
    private final Set<EnumHotbarType> types;
    private final String command;
    private final String name;
    private final int durability;
    private final int slot;

    /**
     * Constructor for the HotbarItems enum
     *
     * @param name       The name of the item
     * @param material   The material of the item
     * @param durability The data of the item
     * @param slot       The slot of the item
     * @param command    The command of the item
     * @param types      The types of the item
     */
    HotbarItems(String name, Material material, int durability, int slot, String command, EnumHotbarType... types) {
        this.name = name;
        this.durability = durability;
        this.slot = slot;
        this.types = new HashSet<>();
        Collections.addAll(this.types, types);
        this.command = command;
        this.material = material;
    }
}