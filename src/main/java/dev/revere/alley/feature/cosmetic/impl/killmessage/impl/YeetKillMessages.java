package dev.revere.alley.feature.cosmetic.impl.killmessage.impl;

import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.impl.killmessage.AbstractKillMessagePack;
import org.bukkit.Material;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@CosmeticData(
        type = EnumCosmeticType.KILL_MESSAGE,
        name = "Yeet Messages",
        description = "For when you don't just kill them, you yeet them.",
        icon = Material.PISTON_BASE,
        slot = 12,
        price = 750
)
public class YeetKillMessages extends AbstractKillMessagePack {
    @Override
    protected String getResourceFileName() {
        return "yeet_messages.yml";
    }
}