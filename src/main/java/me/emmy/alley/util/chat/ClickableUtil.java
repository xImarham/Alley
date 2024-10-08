package me.emmy.alley.util.chat;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 20:16
 */
@UtilityClass
public class ClickableUtil {
    /**
     * Create a clickable message
     *
     * @param message the message
     * @param command the command
     * @param hoverText the hover text
     * @return the text component
     */
    public @NotNull TextComponent createClickableMessage(String message, String command, String hoverText) {
        TextComponent clickableMessage = new TextComponent(CC.translate(message));
        clickableMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        String hover = CC.translate(hoverText);
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();
        clickableMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        return clickableMessage;
    }
}