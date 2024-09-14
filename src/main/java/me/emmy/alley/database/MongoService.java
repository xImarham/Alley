package me.emmy.alley.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.utils.chat.CC;
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
    private final String uri;

    /**
     * Constructor for the MongoService class.
     * @param uri The MongoDB URI.
     */
    public MongoService(String uri) {
        this.uri = uri;
        this.createConnection();
    }

    /**
     * Creates a connection to the MongoDB database.
     */
    private void createConnection() {
        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryWrites(true)
                    .build();

            String databaseName = ConfigHandler.getInstance().getDatabaseConfig().getString("mongo.database");
            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = mongoClient.getDatabase(databaseName);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bAlley&f] &fSuccessfully connected to MongoDB."));
        } catch (Exception e) {
            FileConfiguration config = ConfigHandler.getInstance().getDatabaseConfig();
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bAlley&f] &cFailed to connect to MongoDB. &7(Connection String: " + config.getString("mongo.uri") + ", Database: " + config.getString("mongo.database") + ")"));
            System.exit(2);
        }
    }
}
