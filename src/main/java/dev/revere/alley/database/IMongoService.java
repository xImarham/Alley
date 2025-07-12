package dev.revere.alley.database;

import com.mongodb.client.MongoDatabase;
import dev.revere.alley.plugin.lifecycle.IService;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IMongoService extends IService {

    /**
     * Gets the active MongoDatabase instance for other services to use.
     *
     * @return The MongoDatabase instance.
     * @throws IllegalStateException if the service has not been initialized yet.
     */
    MongoDatabase getMongoDatabase();

}