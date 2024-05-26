package me.emmy.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 15:22
 */

@Getter
@Setter
public class ProfileSettingData {
    private boolean scoreboardEnabled = true;
    private boolean tablistEnabled = true;
    private boolean partyInvitesEnabled = true;
    private boolean partyMessagesEnabled = true;
}
