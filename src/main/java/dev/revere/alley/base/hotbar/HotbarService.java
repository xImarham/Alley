package dev.revere.alley.base.hotbar;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class HotbarService {
    protected final Alley plugin;
    private final Map<EnumHotbarType, List<HotbarItem>> hotbarItemsByType;

    /**
     * Constructor for the HotbarService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public HotbarService(Alley plugin) {
        this.plugin = plugin;
        this.hotbarItemsByType = new EnumMap<>(EnumHotbarType.class);
        Arrays.stream(EnumHotbarType.values()).forEach(type -> this.hotbarItemsByType.put(type, new ArrayList<>()));
        this.initializeHotbarItems();
    }

    /**
     * Initialize the hotbar items.
     */
    private void initializeHotbarItems() {
        Arrays.stream(HotbarItems.values()).forEach(item -> {
            Set<EnumHotbarType> types = item.getTypes();
            if (types != null) {
                types.forEach(type -> this.addHotbarItem(type, item));
            }
        });
    }

    /**
     * Add a hotbar item to the hotbar repository
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     */
    public void addHotbarItem(EnumHotbarType type, HotbarItems hotbarItem) {
        List<HotbarItem> items = this.hotbarItemsByType.computeIfAbsent(type, k -> new ArrayList<>());
        items.add(this.createHotbarItem(type, hotbarItem));
    }

    /**
     * Apply the hotbar items to the player
     *
     * @param player the player
     * @param type   the hotbar type
     */
    public void applyHotbarItems(Player player, EnumHotbarType type) {
        player.getInventory().clear();
        for (HotbarItem item : this.hotbarItemsByType.get(type)) {
            ItemStack itemStack = item.getItemStack();
            if (item.getHotbarItems() == HotbarItems.SETTINGS && itemStack.getDurability() == 3) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                meta.setOwner(player.getName());
                itemStack.setItemMeta(meta);
            }
            itemStack.setDurability((short) item.getHotbarItems().getDurability());
            player.getInventory().setItem(item.getSlot(), itemStack);
        }

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, player::updateInventory, 1L);
    }

    /**
     * Apply the hotbar items to the player
     *
     * @param player the player
     */
    public void applyHotbarItems(Player player) {
        this.applyHotbarItems(player, this.getCorrespondingType(this.plugin.getProfileService().getProfile(player.getUniqueId())));
    }

    /**
     * Get the corresponding hotbar type for the given profile.
     *
     * @param profile the profile
     * @return the corresponding hotbar type
     */
    private EnumHotbarType getCorrespondingType(Profile profile) {
        EnumHotbarType type;

        switch (profile.getState()) {
            case WAITING:
                type = EnumHotbarType.QUEUE;
                break;
            case SPECTATING:
                type = EnumHotbarType.SPECTATOR;
                break;
            case LOBBY:
                if (profile.getParty() != null) {
                    type = EnumHotbarType.PARTY;
                } else {
                    type = EnumHotbarType.LOBBY;
                }
                break;
            case PLAYING_TOURNAMENT:
                type = EnumHotbarType.TOURNAMENT;
                break;
            default:
                type = EnumHotbarType.LOBBY;
                break;
        }

        return type;
    }

    /**
     * Create a hotbar item with the given type and hotbar item
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     * @return the hotbar item
     */
    private HotbarItem createHotbarItem(EnumHotbarType type, HotbarItems hotbarItem) {
        return new HotbarItem(type, hotbarItem,
                new ItemBuilder(hotbarItem.getMaterial())
                        .name(hotbarItem.getName())
                        .hideMeta()
                        .build(),
                hotbarItem.getSlot());
    }

    /**
     * Get a hotbar item by the item stack
     *
     * @param item the item stack
     * @return the hotbar item
     */
    public HotbarItem getItemByStack(ItemStack item) {
        return this.hotbarItemsByType.values().stream()
                .flatMap(List::stream)
                .filter(i -> i.getItemStack().isSimilar(item))
                //.filter(i -> i.getItemStack().equals(item))
                //.filter(i -> i.getItemStack().getDurability() == item.getDurability())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a hotbar item by the hotbar type and hotbar item
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     * @return the hotbar item
     */
    public HotbarItem getItemByStack(EnumHotbarType type, HotbarItems hotbarItem) {
        List<HotbarItem> itemsOfType = this.hotbarItemsByType.get(type);
        if (itemsOfType != null) {
            return itemsOfType.stream()
                    .filter(i -> i.getHotbarItems() == hotbarItem)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}