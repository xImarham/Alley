package me.emmy.alley.kit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 21:49
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Kit {
    private String displayName;
    private String description;
    private String name;

    private ItemStack[] items;
    private ItemStack[] armour;
    private ItemStack icon;

    private List<String> arenas;
    private List<String> types;
}
