package dev.revere.alley.tool.item;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * @author Emmy
 * @project Alley
 * @since 02/05/2025
 */
@UtilityClass
public class ItemStackSerializer {
    /**
     * Serialize an ItemStack array to a Base64 string
     *
     * @param items the ItemStack array to serialize
     * @return the serialized ItemStack array
     */
    public String serializeItemStackArray(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception exception) {
            throw new RuntimeException("Unable to serialize item stacks.", exception);
        }
    }

    /**
     * Deserialize an ItemStack array from a Base64 string
     *
     * @param data the Base64 string to deserialize
     * @return the deserialized ItemStack array
     */
    public ItemStack[] deserializeItemStackArray(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (Exception exception) {
            throw new RuntimeException("Unable to deserialize item stacks.", exception);
        }
    }
}