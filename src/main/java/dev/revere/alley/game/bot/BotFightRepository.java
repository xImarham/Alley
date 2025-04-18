package dev.revere.alley.game.bot;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.game.bot.enums.EnumBotPreset;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 18/04/2025
 */
@Getter
public class BotFightRepository {
    private final List<BotFight> botFights;

    public BotFightRepository() {
        this.botFights = new ArrayList<>();
    }

    /**
     * Adds a bot fight to the repository.
     *
     * @param botFight The BotFight instance to add.
     */
    public void addBotFight(BotFight botFight) {
        this.botFights.add(botFight);
    }

    /**
     * Removes a bot fight from the repository.
     *
     * @param botFight The BotFight instance to remove.
     */
    public void removeBotFight(BotFight botFight) {
        this.botFights.remove(botFight);
    }

    /**
     * Create a match with the given player, arena, and kit.
     *
     * @param player The player who is creating the match.
     * @param arena  The arena where the match will take place.
     * @param kit    The kit to be used in the match.
     */
    public void createMatch(Player player, AbstractArena arena, Kit kit) {
        EnumBotPreset preset = EnumBotPreset.EASY;
        Bot bot = new Bot(preset, arena.getPos2(), "&bAlley Bot");

        //TODO: Don't allow all kits, might need to add a new boolean field in the kit object to allow bot fights BASED on which
        // settings are enabled. I am NOT doing arcade kits. THAT IS NOT HAPPENING. I MIGHT EVEN REVERT THIS COMMIT TOO BECAUSE IM ALREADY PISSED.

        Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> {
            BotFight botFight = new BotFight(player, bot, preset, kit, arena);
            botFight.startFight();
        }, 2L);
    }
}