package dev.revere.alley.locale;

import lombok.experimental.UtilityClass;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:49
 */
@UtilityClass
public class ErrorMessage {
    public String DEBUG = CC.translate("&4(&cDebug&4) &fSomething has happened.");
    public String DEBUG_STILL_IN_DEVELOPMENT = CC.translate("&cThis is still in development.");
    public String DEBUG_CMD = "&cThis command is not yet implemented due to it being in development.";
    public String PLAYER_NOT_ONLINE = "&cNo player matching &4{player} &cis connected to this server.";
    public String PLAYER_NEVER_PLAYED_BEFORE = "&cThat player has never played on this server before.";
    public String CANNOT_INVITE_SELF = "&cYou cannot invite yourself to a party.";
    public String TARGET_HAS_NO_PARTY = "&b{player} is not in a party.";
}