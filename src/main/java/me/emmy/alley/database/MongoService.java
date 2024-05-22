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

import java.util.Objects;

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

    public MongoService(String uri) {
        this.uri = uri;
        this.createConnection();
    }

    private void createConnection() {
        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryWrites(true)
                    .build();

            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = mongoClient.getDatabase(Alley.getInstance().getConfigHandler().getConfigByName("database/database.yml").getString("mongo.database"));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&cAlley&f] &cMongoDB failed to create a connection."));
            e.printStackTrace();
        }
    }
}
