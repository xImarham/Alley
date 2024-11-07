package dev.revere.alley.kit.settings.impl;

import dev.revere.alley.kit.settings.KitSetting;
import dev.revere.alley.kit.settings.annotation.KitSettingData;

/**
 * @author Emmy
 * @project Alley
 * @date 27/08/2024 - 19:08
 */
@KitSettingData(name = "DenyMovement", description = "This denies the player movement during countdown.", enabled = false)
public class KitSettingDenyMovementImpl extends KitSetting {
}