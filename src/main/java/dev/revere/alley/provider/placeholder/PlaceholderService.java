package dev.revere.alley.provider.placeholder;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.provider.placeholder.impl.AlleyPlaceholderImpl;
import dev.revere.alley.tool.logger.Logger;

/**
 * @author Emmy
 * @project alley-practice
 * @since 17/07/2025
 */
@Service(provides = IPlaceholderService.class, priority = 430)
public class PlaceholderService implements IPlaceholderService {

    @Override
    public void initialize(AlleyContext context) {
        this.registerExpansion(context.getPlugin());
    }

    @Override
    public void registerExpansion(Alley plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Logger.info("PlaceholderAPI is not installed! Alley Placeholder Expansion will not be registered.");
            return;
        }

        Logger.logTime(AlleyPlaceholderImpl.class.getSimpleName(), () -> {
            AlleyPlaceholderImpl expansion = new AlleyPlaceholderImpl(plugin);
            expansion.register();
        });
    }
}