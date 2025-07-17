package dev.revere.alley.adapter.knockback;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.adapter.knockback.enums.EnumKnockbackType;
import dev.revere.alley.adapter.knockback.impl.DefaultKnockbackImpl;
import dev.revere.alley.adapter.knockback.impl.ZoneKnockbackImpl;
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