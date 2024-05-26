package me.emmy.alley.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 21/05/2024 - 21:40
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

            String databaseName = Alley.getInstance().getConfigHandler().getConfigByName("database/database.yml").getString("mongo.database");
            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = mongoClient.getDatabase(databaseName);

            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&dAlley&f] &aMongoDB connection established."));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&dAlley&f] &cMongoDB failed to create a connection."));
        }
    }
}
