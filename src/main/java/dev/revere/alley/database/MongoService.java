package dev.revere.alley.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 21:40
 */
@Getter
public class MongoService {
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
    private final String databaseName;
    private final String uri;

    /**
     * Constructor for the MongoService class.
     *
     * @param uri          The MongoDB URI.
     * @param databaseName The name of the database.
     */
    public MongoService(String uri, String databaseName) {
        this.uri = uri;
        this.databaseName = databaseName;
        this.createConnection();
    }

    /**
     * Creates a connection to the MongoDB database.
     */
    private void createConnection() {
        try {
            ConnectionString connectionString = new ConnectionString(this.uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                                               .applyConnectionString(connectionString)
                                               .retryWrites(true)
                                               .build();

            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = this.mongoClient.getDatabase(this.databaseName);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bAlley&f] &fSuccessfully connected to MongoDB."));
        } catch (Exception e) {
            FileConfiguration config = Alley.getInstance().getConfigService().getDatabaseConfig();
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bAlley&f] &cFailed to connect to MongoDB. &7(Connection String: " + config.getString("mongo.uri") + ", Database: " + config.getString("mongo.database") + ")"));
            System.exit(2);
        }
    }
}