package dev.revere.alley;

import dev.revere.alley.api.menu.MenuListener;
import dev.revere.alley.base.arena.listener.ArenaListener;
import dev.revere.alley.base.combat.listener.CombatListener;
import dev.revere.alley.base.hotbar.listener.HotbarListener;
import dev.revere.alley.base.queue.listener.QueueListener;
import dev.revere.alley.base.spawn.listener.SpawnListener;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.lifecycle.IService;
import dev.revere.alley.feature.emoji.listener.EmojiListener;
import dev.revere.alley.feature.layout.listener.LayoutListener;
import dev.revere.alley.game.ffa.listener.FFAListener;
import dev.revere.alley.game.ffa.listener.impl.FFACuboidListener;
import dev.revere.alley.game.match.listener.MatchListener;
import dev.revere.alley.game.match.listener.impl.*;
import dev.revere.alley.game.match.snapshot.listener.SnapshotListener;
import dev.revere.alley.game.party.listener.PartyListener;
import dev.revere.alley.profile.listener.ProfileListener;
import dev.revere.alley.provider.expansion.AlleyPlaceholderExpansion;
import dev.revere.alley.server.listener.CoreChatListener;
import dev.revere.alley.task.ArrowRemovalTask;
import dev.revere.alley.task.MatchPearlCooldownTask;
import dev.revere.alley.task.RepositoryCleanupTask;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.logger.PluginLogger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Alley – A modern, modular Practice PvP knockback built from the ground up for Minecraft 1.8.
 * <p>
 * Developed by Revere Inc., Alley focuses on clean, professional, and readable code,
 * making it easy for developers to jump into practice PvP development with minimal friction.
 * </p>
 * <p>
 * Alley is open source under the terms described in the README:
 * <a href="https://github.com/RevereInc/alley-practice">GitHub Repository</a>
 * </p>
 *
 * @author Emmy, Remi
 * @version 2.0 — Complete recode (entirely rewritten from scratch)
 * @see <a href="https://revere.dev">revere.dev</a>
 * @see <a href="https://discord.gg/revere">Discord Support</a>
 * @since 19/04/2024
 */
@Getter
public class Alley extends JavaPlugin {

    @Getter
    private static Alley instance;
    private AlleyContext context;

    public Alley() {
        new AlleyAPI();
    }

    @Override
    public void onEnable() {
        instance = this;
        long start = System.currentTimeMillis();

        this.checkDescription();

        try {
            this.context = new AlleyContext(this);
            this.context.initialize();
        } catch (Exception e) {
            Logger.logException("A fatal error occurred during service initialization. Alley will be disabled.", e);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.registerExpansion();
        this.registerListeners();
        this.runTasks();

        // CommandUtility.registerCommands();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        PluginLogger.onEnable(timeTaken);

        AlleyAPI.getInstance().runOnEnableCallbacks();
    }

    @Override
    public void onDisable() {
        if (this.context != null) {
            this.context.shutdown();
        }

        PluginLogger.onDisable();

        AlleyAPI.getInstance().runOnDisableCallbacks();
    }

    /**
     * Provides global, type-safe access to any managed service via its interface.
     *
     * @param serviceInterface The class of the service interface you want (e.g., IProfileService.class).
     * @return The service instance.
     * @throws IllegalStateException if the service is not found.
     */
    public <T extends IService> T getService(Class<T> serviceInterface) {
        if (this.context == null) {
            throw new IllegalStateException("AlleyContext is not available. The plugin may be disabling or failed to load.");
        }
        return this.context.getService(serviceInterface)
                .orElseThrow(() -> new IllegalStateException("Could not find a registered service for: " + serviceInterface.getSimpleName()));
    }

    private void checkDescription() {
        List<String> authors = this.getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emmy", "Remi");
        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
        }
    }

    private void registerExpansion() {
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Logger.logTime(AlleyPlaceholderExpansion.class.getSimpleName(), () -> {
                AlleyPlaceholderExpansion expansion = new AlleyPlaceholderExpansion(this);
                expansion.register();
            });
        }
    }

    private void registerListeners() {
        Arrays.asList(
                new ProfileListener(),
                new HotbarListener(),
                new PartyListener(),
                new MatchListener(),
                new MatchInteractListener(),
                new MatchPearlListener(),
                new MatchDisconnectListener(),
                new MatchDamageListener(),
                new MatchChatListener(),
                new MatchBlockListener(),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),
                new FFAListener(),
                new FFACuboidListener(),
                new EmojiListener(),
                new CombatListener(),
                new QueueListener(),
                new CoreChatListener(),
                new LayoutListener(),
                new SnapshotListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void runTasks() {
        final Map<String, Runnable> tasks = new LinkedHashMap<>();

        tasks.put(RepositoryCleanupTask.class.getSimpleName(), () -> new RepositoryCleanupTask(this).runTaskTimer(this, 0L, 40L));
        tasks.put(MatchPearlCooldownTask.class.getSimpleName(), () -> new MatchPearlCooldownTask().runTaskTimer(this, 2L, 2L));
        tasks.put(ArrowRemovalTask.class.getSimpleName(), () -> new ArrowRemovalTask().runTaskTimer(this, 20L, 20L));

        /* if (this.configService.getTabListConfig().getBoolean("tablist.enabled")) {
            tasks.put(TablistUpdateTask.class.getSimpleName(), () -> new TablistUpdateTask().runTaskTimer(this, 0L, 20L));
        } */

        tasks.forEach(Logger::logTimeTask);
    }
}