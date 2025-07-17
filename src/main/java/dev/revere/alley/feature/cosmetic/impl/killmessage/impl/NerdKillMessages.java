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
        name = "Nerd Messages",
        description = "Debug your opponents out of existence.",
        icon = Material.COMMAND,
        slot = 13,
        price = 750
)
public class NerdKillMessages extends AbstractKillMessagePack {
    @Override
    protected String getResourceFileName() {
        return "nerd_messages.yml";
    }
}