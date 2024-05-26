package me.emmy.alley.database.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MongoUtility {
    private static final Gson gson = new GsonBuilder().create();

    /**
     * Converts an object to a Document.
     *
     * @param obj The object to convert.
     * @return The converted Document.
     */
    public static Document toDocument(Object obj) {
        String json = gson.toJson(obj);
        return Document.parse(json);
    }

    /**
     * Converts a Document to a specified class.
     *
     * @param document The document to convert.
     * @param clazz    The class to convert to.
     * @param <T>      The type of the class.
     * @return The converted class.
     */
    public static <T> T fromDocument(Document document, Class<T> clazz) {
        String json = document.toJson();
        return gson.fromJson(json, clazz);
    }
}
