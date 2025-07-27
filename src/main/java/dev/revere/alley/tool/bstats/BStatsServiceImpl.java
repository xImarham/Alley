package dev.revere.alley.tool.bstats;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.annotation.Service;
import org.bstats.bukkit.Metrics;
import dev.revere.alley.plugin.AlleyContext;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/07/2025
 */
@Service(provides = BStatsService.class, priority = 1)
public class BStatsServiceImpl implements BStatsService {
    @Override
    public void setup(AlleyContext context) {
        Metrics metrics = new Metrics(Alley.getInstance(), 26678);
    }
}
