package dev.revere.alley.server;

import dev.revere.alley.Alley;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.server.enums.EnumCoreType;
import dev.revere.alley.server.impl.AquaCoreImpl;
import dev.revere.alley.server.impl.DefaultCoreImpl;
import dev.revere.alley.server.impl.HeliumCoreImpl;
import dev.revere.alley.server.impl.PhoenixCoreImpl;
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
@Service(provides = ICoreAdapter.class, priority = 60)
public class CoreAdapter implements ICoreAdapter {

    private final Alley plugin;
    private ICore core;

    /**
     * Constructor for DI. Receives the main plugin instance.
     */
    public CoreAdapter(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.core = this.determineCore();
    }

    @Override
    public ICore getCore() {
        if (this.core == null) {
            throw new IllegalStateException("CoreAdapter has not been initialized yet.");
        }
        return this.core;
    }

    /**
     * Determines the server implementation to use based on the enabled plugins.
     *
     * @return The selected server implementation.
     */
    private ICore determineCore() {
        ICore selectedCore = new DefaultCoreImpl(this.plugin);

        for (EnumCoreType coreType : EnumCoreType.values()) {
            if (this.plugin.getServer().getPluginManager().isPluginEnabled(coreType.getPluginName())) {
                switch (coreType) {
                    case PHOENIX:
                        selectedCore = new PhoenixCoreImpl(SharedAPI.getInstance());
                        break;
                    case AQUA:
                        selectedCore = new AquaCoreImpl(AquaCoreAPI.INSTANCE, this.plugin);
                        break;
                    case HELIUM:
                        selectedCore = new HeliumCoreImpl(HeliumAPI.INSTANCE);
                        break;
                }
                break;
            }
        }
        return selectedCore;
    }
}