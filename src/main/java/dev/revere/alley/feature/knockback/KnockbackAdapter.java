package dev.revere.alley.feature.knockback;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.knockback.enums.EnumKnockbackType;
import dev.revere.alley.feature.knockback.impl.DefaultKnockbackImpl;
import dev.revere.alley.feature.knockback.impl.ZoneKnockbackImpl;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Setter
public class KnockbackAdapter {
    protected final Alley plugin;
    private final IKnockback knockbackType;

    /**
     * Constructor for the KnockbackAdapter class.
     *
     * @param plugin The Alley plugin instance.
     */
    public KnockbackAdapter(Alley plugin) {
        this.plugin = plugin;
        this.knockbackType = this.determineKnockback();
    }

    /**
     * Determines the knockback implementation to use based on the enabled plugins.
     *
     * @return The selected knockback implementation.
     */
    private IKnockback determineKnockback() {
        IKnockback selectedCore = new DefaultKnockbackImpl();

        for (EnumKnockbackType kbType : EnumKnockbackType.values()) {
            if (this.plugin.getServer().getName().equalsIgnoreCase(kbType.getSpigotName())) {
                switch (kbType) {
                    case ZONE:
                        selectedCore = new ZoneKnockbackImpl();
                        break;
                }
                break;
            }
        }

        Logger.log("Adapting to &6" + selectedCore.getType().getSpigotName() + " &fby &6" + selectedCore.getType().getSpigotAuthor() + "&f.");
        return selectedCore;
    }
}