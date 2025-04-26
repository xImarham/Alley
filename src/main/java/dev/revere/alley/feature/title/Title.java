package dev.revere.alley.feature.title;

import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.kit.Kit;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@Getter
public class Title {
    private final Kit kit;
    private final String prefix;
    private final Division requiredDivision;

    /**
     * Constructor for the Title class.
     *
     * @param kit              The kit associated with the title.
     * @param prefix           The prefix of the title.
     * @param requiredDivision The required division for the title.
     */
    public Title(Kit kit, String prefix, Division requiredDivision) {
        this.kit = kit;
        this.prefix = prefix;
        this.requiredDivision = requiredDivision;
    }
}