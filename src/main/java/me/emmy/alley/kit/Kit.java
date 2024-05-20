package me.emmy.alley.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
@Getter
@Setter
@AllArgsConstructor
public class Kit {
    private String name;
    private String displayName;
    private String description;
    private boolean enabled;
    private int unrankedslot;
    private int rankedslot;
    private int editorslot;
    private ItemStack[] inventory;
    private ItemStack[] armor;
    private Material icon;
    private int iconData;
}
