package me.emmy.alley.database.profile;

import me.emmy.alley.profile.Profile;

/**
 * @author Remi
 * @project Alley
 * @date 5/22/2024
 */
public interface IProfile {

    void saveProfile(Profile profile);
    void loadProfile(Profile profile);
}
