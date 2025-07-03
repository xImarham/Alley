package dev.revere.alley.feature.title;

import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.core.lifecycle.IService;
import dev.revere.alley.feature.title.record.TitleRecord;

import java.util.Map;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ITitleService extends IService {
    /**
     * Gets the map of all loaded titles.
     * @return A map where the key is the Kit and the value is the TitleRecord.
     */
    Map<Kit, TitleRecord> getTitles();
}
