package dev.revere.alley.core;

import dev.revere.alley.Alley;
import dev.revere.alley.core.enums.EnumCoreType;
import dev.revere.alley.core.impl.AquaCoreImpl;
import dev.revere.alley.core.impl.DefaultCoreImpl;
import dev.revere.alley.core.impl.HeliumCoreImpl;
import dev.revere.alley.core.impl.PhoenixCoreImpl;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import me.activated.core.plugin.AquaCoreAPI;
import services.plasma.helium.api.HeliumAPI;
import xyz.refinedev.phoenix.SharedAPI;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Setter
public class CoreAdapter {
    protected final Alley plugin;
    private ICore core;

    /**
     * Constructor for the CoreAdapter class.
     *
     * @param plugin The Alley plugin instance.
     */
    public CoreAdapter(Alley plugin) {
        this.plugin = plugin;
        this.core = this.determineCore();
    }

    /**
     * Determines the core implementation to use based on the enabled plugins.
     *
     * @return The selected core implementation.
     */
    private ICore determineCore() {
        ICore selectedCore = new DefaultCoreImpl(Alley.getInstance());

        for (EnumCoreType coreType : EnumCoreType.values()) {
            if (this.plugin.getServer().getPluginManager().isPluginEnabled(coreType.getPluginName())) {
                switch (coreType) {
                    case PHOENIX:
                        selectedCore = new PhoenixCoreImpl(SharedAPI.getInstance());
                        break;
                    case AQUA:
                        selectedCore = new AquaCoreImpl(AquaCoreAPI.INSTANCE);
                        break;
                    case HELIUM:
                        selectedCore = new HeliumCoreImpl(HeliumAPI.INSTANCE);
                }
                break;
            }
        }

        Logger.log("Adapting to &b" + selectedCore.getType().getPluginName() + " Core &fby &b" + selectedCore.getType().getPluginAuthor() + "&f.");
        return selectedCore;
    }
}