package dev.revere.alley.essential.emoji;

import dev.revere.alley.essential.emoji.enums.EnumEmojiType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:41
 */
@Getter
public class EmojiRepository {
    private final Map<String, String> symbolReplacements;

    public EmojiRepository() {
        this.symbolReplacements = new HashMap<>();

        for (EnumEmojiType value : EnumEmojiType.values()) {
            this.symbolReplacements.put(value.getIdentifier(), value.getFormat());
        }
    }
}