package dev.revere.alley.essential.chat;

import dev.revere.alley.essential.chat.chat.EnumEmojiType;
import dev.revere.alley.util.chat.Symbol;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:41
 */
@Getter
public class ChatService {
    private final Map<String, String> symbolReplacements;

    public ChatService() {
        this.symbolReplacements = new HashMap<>();

        for (EnumEmojiType value : EnumEmojiType.values()) {
            this.symbolReplacements.put(value.getIdentifier(), value.getFormat());
        }
    }
}