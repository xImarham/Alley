package dev.revere.alley.base.hotbar;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.profile.IProfileService;
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
@Service(provides = IHotbarService.class, priority = 190)
public class HotbarService implements IHotbarService {
    private final Alley plugin;
    private final IProfileService profileService;

    private final Map<EnumHotbarType, List<HotbarItem>> hotbarItemsByType = new EnumMap<>(EnumHotbarType.class);

    /**
     * Constructor for DI.
     */
    public HotbarService(Alley plugin, IProfileService profileService) {
        this.plugin = plugin;
        this.profileService = profileService;
    }


    @Override
    public void initialize(AlleyContext context) {
        Arrays.stream(EnumHotbarType.values()).forEach(type -> this.hotbarItemsByType.put(type, new ArrayList<>()));
        this.initializeHotbarItems();
    }

    /**
     * Initialize the hotbar items.
     */
    private void initializeHotbarItems() {
        Arrays.stream(HotbarItems.values()).forEach(item -> {
            Set<EnumHotbarType> types = item.getTypes();
            types.forEach(type -> this.addHotbarItem(type, item));
        });
    }

    @Override
    public void applyHotbarItems(Player player, EnumHotbarType type) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        List<HotbarItem> itemsToApply = this.hotbarItemsByType.get(type);
        if (itemsToApply == null) return;

        for (HotbarItem item : this.hotbarItemsByType.get(type)) {
            ItemStack itemStack = item.getItemStack().clone();

            if (item.getHotbarItems() == HotbarItems.SETTINGS && itemStack.getItemMeta() instanceof SkullMeta) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                meta.setOwner(player.getName());
                itemStack.setItemMeta(meta);
            }

            itemStack.setDurability((short) item.getHotbarItems().getDurability());
            player.getInventory().setItem(item.getSlot(), itemStack);
        }

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, player::updateInventory, 1L);
    }

    @Override
    public void applyHotbarItems(Player player) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        this.applyHotbarItems(player, this.getCorrespondingType(profile));
    }

    @Override
    public HotbarItem getItemByStack(ItemStack item) {
        if (item == null) return null;
        return this.hotbarItemsByType.values().stream()
                .flatMap(List::stream)
                .filter(i -> i.getItemStack().isSimilar(item))
                .findFirst()
                .orElse(null);
    }

    @Override
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
                return (profile.getParty() != null) ? EnumHotbarType.PARTY : EnumHotbarType.LOBBY;
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
}