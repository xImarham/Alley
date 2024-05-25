package me.emmy.alley.profile.settings.player;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 15:22
 */

@Getter
@Setter
public class PlayerSettings {

    private boolean scoreboardEnabled = true;
    private boolean tablistEnabled = true;
    private boolean partyInvitesEnabled = true;
    private boolean partyMessagesEnabled = true;
}
