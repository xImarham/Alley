package dev.revere.alley.feature.knockback;

import dev.revere.alley.Alley;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.feature.knockback.enums.EnumKnockbackType;
import dev.revere.alley.feature.knockback.impl.DefaultKnockbackImpl;
import dev.revere.alley.feature.knockback.impl.ZoneKnockbackImpl;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Service(provides = IKnockbackAdapter.class, priority = 50)
public class KnockbackAdapter implements IKnockbackAdapter {
    private final Alley plugin;
    private IKnockback knockbackImplementation;

    /**
     * Constructor for DI. Receives the main plugin instance.
     */
    public KnockbackAdapter(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.knockbackImplementation = this.determineKnockback();
        Logger.info("Adapting to &6" + knockbackImplementation.getType().getSpigotName() + " &fby &6" + knockbackImplementation.getType().getSpigotAuthor() + "&f.");
    }

    @Override
    public IKnockback getKnockbackImplementation() {
        if (this.knockbackImplementation == null) {
            throw new IllegalStateException("KnockbackAdapter has not been initialized yet.");
        }
        return this.knockbackImplementation;
    }

    /**
     * Determines the knockback implementation to use based on the server's name.
     *
     * @return The selected knockback implementation.
     */
    private IKnockback determineKnockback() {
        IKnockback selectedImplementation = new DefaultKnockbackImpl();

        for (EnumKnockbackType kbType : EnumKnockbackType.values()) {
            if (this.plugin.getServer().getName().equalsIgnoreCase(kbType.getSpigotName())) {
                switch (kbType) {
                    case ZONE:
                        selectedImplementation = new ZoneKnockbackImpl();
                        break;
                }
                break;
            }
        }

        return selectedImplementation;
    }
}