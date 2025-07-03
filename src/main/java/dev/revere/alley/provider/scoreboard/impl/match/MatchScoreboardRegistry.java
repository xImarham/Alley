package dev.revere.alley.provider.scoreboard.impl.match;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.setting.KitSetting;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.tool.logger.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
public class MatchScoreboardRegistry {
    private final Map<Class<? extends KitSetting>, IMatchScoreboard> kitSettingScoreboards = new HashMap<>();
    private final Map<Class<? extends AbstractMatch>, IMatchScoreboard> matchTypeScoreboards = new HashMap<>();
    private IMatchScoreboard defaultScoreboard;

    /**
     * Scans the classpath to discover and register all annotated scoreboard providers.
     * This should be called once by the object that creates the registry.
     */
    public void initialize() {
        String searchPackage = "dev.revere.alley.provider.scoreboard";
        long startTime = System.nanoTime();

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(searchPackage).scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(ScoreboardData.class.getName())) {
                if (classInfo.isAbstract() || classInfo.isInterface()) {
                    continue;
                }

                try {
                    Class<?> clazz = classInfo.loadClass();
                    if (!IMatchScoreboard.class.isAssignableFrom(clazz)) {
                        continue;
                    }

                    // CORRECT: Get the no-argument constructor now.
                    Constructor<?> constructor = clazz.getConstructor();
                    // CORRECT: Create instance without passing the plugin.
                    // The individual scoreboard providers will use Alley.getInstance().getService(...)
                    IMatchScoreboard scoreboard = (IMatchScoreboard) constructor.newInstance();
                    ScoreboardData annotation = clazz.getAnnotation(ScoreboardData.class);

                    if (annotation.isDefault()) {
                        this.defaultScoreboard = scoreboard;
                    } else if (annotation.kit() != KitSetting.class) {
                        kitSettingScoreboards.put(annotation.kit(), scoreboard);
                    } else if (annotation.match() != AbstractMatch.class) {
                        matchTypeScoreboards.put(annotation.match(), scoreboard);
                    }
                } catch (Exception e) {
                    Logger.logException("Failed to instantiate scoreboard provider: " + classInfo.getName(), e);
                }
            }
        }

        long duration = (System.nanoTime() - startTime) / 1_000_000;
        int total = kitSettingScoreboards.size() + matchTypeScoreboards.size() + (defaultScoreboard != null ? 1 : 0);
        Logger.info("Discovered and registered " + total + " match scoreboard providers in " + duration + "ms.");
    }

    /**
     * Resolves the appropriate scoreboard for the given match.
     *
     * @param match The match to resolve.
     * @return The resolved IMatchScoreboard.
     */
    public IMatchScoreboard getScoreboard(AbstractMatch match) {
        for (Class<? extends KitSetting> settingClass : kitSettingScoreboards.keySet()) {
            if (match.getKit().isSettingEnabled(settingClass)) {
                return kitSettingScoreboards.get(settingClass);
            }
        }

        IMatchScoreboard scoreboard = matchTypeScoreboards.get(match.getClass());
        if (scoreboard != null) {
            return scoreboard;
        }

        return defaultScoreboard;
    }
}