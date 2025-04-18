package dev.revere.alley.game.bot;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.game.bot.enums.EnumBotFightState;
import dev.revere.alley.game.bot.enums.EnumBotPreset;
import dev.revere.alley.game.bot.runnable.BotFightActionRunnable;
import dev.revere.alley.game.bot.runnable.BotFightRunnable;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@Getter
@Setter
public class BotFight {
    private final Player player;

    private final Bot bot;
    private final Kit kit;
    private final AbstractArena arena;

    private final EnumBotPreset preset;
    private EnumBotFightState state;

    private BotFightRunnable runnable;
    private BotFightActionRunnable actionRunnable;

    /**
     * Constructor for the BotFight class.
     *
     * @param player The player involved in the fight.
     * @param bot    The bot involved in the fight.
     * @param preset The preset for the bot.
     * @param kit    The kit used in the fight.
     * @param arena  The arena where the fight takes place.
     */
    public BotFight(Player player, Bot bot, EnumBotPreset preset, Kit kit, AbstractArena arena) {
        this.player = player;
        this.bot = bot;
        this.preset = preset;
        this.kit = kit;
        this.arena = arena;
        Alley.getInstance().getBotFightRepository().addBotFight(this);
    }

    public void startFight() {
        this.sendMessage("&7[&bBot Fight&7] &a" + this.player.getName() + " &bvs &a" + this.bot.getNpc().getName() + "!");

        if (this.runnable != null) {
            this.runnable.cancel();
        }

        Profile profile = Alley.getInstance().getProfileService().getProfile(this.player.getUniqueId());
        profile.setState(EnumProfileState.FIGHTING_BOT);
        profile.setBotFight(this);

        this.state = EnumBotFightState.STARTING;
        this.runnable = new BotFightRunnable(this);
        this.runnable.runTaskTimer(Alley.getInstance(), 0L, 20L);
        Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), this::setupPlayer, 2L);
    }

    public void endFight() {
        if (this.runnable != null) {
            this.runnable.cancel();
        }

        if (this.actionRunnable != null) {
            this.actionRunnable.cancel();
        }

        Alley plugin = Alley.getInstance();
        Profile profile = plugin.getProfileService().getProfile(this.player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);

        plugin.getSpawnService().teleportToSpawn(this.player);
        plugin.getHotbarService().applyHotbarItems(this.player, HotbarType.LOBBY);
        Alley.getInstance().getBotFightRepository().removeBotFight(this);
    }

    public void setupPlayer() {
        PlayerUtil.reset(this.player, true);

        this.player.getInventory().clear();
        this.player.getInventory().setContents(this.kit.getInventory());
        this.player.getInventory().setArmorContents(this.kit.getArmor());
        this.kit.applyPotionEffects(this.player);

        this.player.teleport(this.arena.getPos1());
    }

    public void setupBot() {
        Entity botEntity = this.bot.getNpc().getEntity();
        Player npcPlayer = (Player) botEntity;
        npcPlayer.getInventory().clear();
        npcPlayer.getInventory().setContents(this.kit.getInventory());
        npcPlayer.getInventory().setArmorContents(this.kit.getArmor());
        npcPlayer.updateInventory();

        this.kit.applyPotionEffects(npcPlayer);

        npcPlayer.teleport(this.arena.getPos2());
    }

    /**
     * Handles the disconnect of a player during the fight.
     *
     * @param player The player who disconnected.
     */
    public void handleDisconnect(Player player) {

    }

    /**
     * Handles the death of a player during the fight.
     *
     * @param player The player who died.
     */
    public void handleDeath(Player player) {
        if (this.state == EnumBotFightState.FIGHTING) {
            this.state = EnumBotFightState.ENDING;

            if (player.equals(this.player)) {
                this.sendMessage("&cYou died! Bot wins.");
            } else {
                this.sendMessage("&aBot was defeated! You win.");
            }
        }

        if (this.bot.getNpc().isSpawned()) {
            this.bot.destroy();
        }
    }

    /**
     * Sends a message to the player.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        this.player.sendMessage(CC.translate(message));
    }
}