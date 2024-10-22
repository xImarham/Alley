package me.emmy.alley.profile.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 11:57
 */
@Getter
public enum EnumChatChannel {

    GLOBAL("Global", "The players chat channel is set to default."),
    PARTY("Party", "The players chat channel is set to party.")

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumChatChannel class.
     *
     * @param name        The name of the chat channel.
     * @param description The description of the chat channel.
     */
    EnumChatChannel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get the chat channels sorted.
     *
     * @return The chat channels sorted.
     */
    public static String getChatChannelsSorted() {
        StringBuilder stringBuilder = new StringBuilder();
        for (EnumChatChannel chatChannel : EnumChatChannel.values()) {
            stringBuilder.append(chatChannel.getName()).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    /**
     * Check if a chat channel exists.
     *
     * @param chatChannel The chat channel to check.
     * @return True if the chat channel exists, otherwise false.
     */
    public static boolean doesExist(String chatChannel) {
        for (EnumChatChannel enumChatChannel : EnumChatChannel.values()) {
            if (enumChatChannel.getName().equalsIgnoreCase(chatChannel)) {
                return true;
            }
        }
        return false;
    }
}