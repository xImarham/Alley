package dev.revere.alley.feature.title;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.title.record.TitleRecord;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.visual.TextFormatter;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@Getter
public class TitleService {
    protected final Alley plugin;
    private final Map<Kit, TitleRecord> titles;

    /**
     * Constructor for the TitleService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public TitleService(Alley plugin) {
        this.plugin = plugin;
        this.titles = new HashMap<>();
        this.loadTitles();
    }

    private void loadTitles() {
        FileConfiguration config = this.plugin.getConfigService().getTitlesConfig();
        String path = "titles.";

        AtomicInteger missingKits = new AtomicInteger();
        this.plugin.getKitService().getKits().forEach(kit -> {
            if (!this.isKitPresentInConfig(config, kit)) {
                missingKits.getAndIncrement();
                config.set(path + kit.getName() + ".prefix", this.getPrefixBasedOnHighestDivision(kit));
                config.set(path + kit.getName() + ".required_division", this.plugin.getDivisionService().getHighestDivision().getName());
            }
        });

        if (missingKits.get() > 0) {
            TextFormatter.centerText(
                    Arrays.asList(
                            "",
                            "&c&lINFO",
                            "&fMissing &c" + missingKits + " &fkits in titles.yml.",
                            "&fDefault values have been applied.",
                            "&fPlease check the &cfile &fand adjust as needed.",
                            ""
                    ),
                    60
            ).forEach(line -> Bukkit.getConsoleSender().sendMessage(CC.translate(line)));
        }

        this.plugin.getKitService().getKits().forEach(kit -> {
            String prefix = config.getString(path + kit.getName() + ".prefix");
            String requiredDivisionName = config.getString(path + kit.getName() + ".required_division");

            Division requiredDivision = this.plugin.getDivisionService().getDivision(requiredDivisionName);
            if (requiredDivision == null) {
                Logger.logError("Division " + requiredDivisionName + " for kit " + kit.getName() + " does not exist.");
            } else {
                TitleRecord title = new TitleRecord(kit, prefix, requiredDivision);
                this.titles.put(kit, title);
            }
        });

        File titlesFile = this.plugin.getConfigService().getConfigFile("storage/titles.yml");
        this.plugin.getConfigService().saveConfig(titlesFile, config);
    }

    /**
     * Checks if a kit is present in the configuration.
     *
     * @param config The configuration to check.
     * @param kit    The kit to check for.
     * @return True if the kit is present in the configuration, false otherwise.
     */
    private boolean isKitPresentInConfig(FileConfiguration config, Kit kit) {
        return config.contains("titles." + kit.getName());
    }

    /**
     * Gets the title for a specific kit.
     *
     * @param kit The kit to get the title for.
     * @return The title for the specified kit.
     */
    private String getPrefixBasedOnHighestDivision(Kit kit) {
        Division highestDivision = this.plugin.getDivisionService().getHighestDivision();

        return "&b&l" + kit.getName().toUpperCase() + " " + highestDivision.getName().toUpperCase();
    }
}