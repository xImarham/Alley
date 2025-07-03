package dev.revere.alley.feature.emoji;

import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.feature.emoji.enums.EnumEmojiType;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:41
 */
@Getter
@Service(provides = IEmojiRepository.class, priority = 420)
public class EmojiRepository implements IEmojiRepository {
    private final Map<String, String> emojis = new HashMap<>();

    @Override
    public void initialize(AlleyContext context) {
        for (EnumEmojiType value : EnumEmojiType.values()) {
            this.emojis.put(value.getIdentifier(), value.getFormat());
        }
        Logger.info("Loaded " + this.emojis.size() + " emojis.");
    }

    @Override
    public Map<String, String> getEmojis() {
        return Collections.unmodifiableMap(this.emojis);
    }

    @Override
    public Optional<String> getEmojiFormat(String identifier) {
        return Optional.ofNullable(this.emojis.get(identifier));
    }
}