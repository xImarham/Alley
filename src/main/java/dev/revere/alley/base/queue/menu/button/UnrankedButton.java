package dev.revere.alley.base.queue.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.PlayerUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class UnrankedButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = this.queue.getKit();

        return new ItemBuilder(kit.getIcon())
                .name(kit.getDisplayName())
                .durability(kit.getDurability())
                .hideMeta()
                .lore(this.getLore(kit))
                .hideMeta().build();
    }

    /**
     * Get the lore for the kit.
     *
     * @param kit the kit to get the lore for
     * @return the lore for the kit
     */
    private @NotNull List<String> getLore(Kit kit) {
        List<String> lore = new ArrayList<>();
        if (!kit.getDescription().isEmpty()) {
            Collections.addAll(lore,
                    "&7" + kit.getDescription(),
                    ""
            );
        }

        Collections.addAll(lore,
                "&fPlaying: &b" + this.queue.getQueueFightCount(),
                "&fQueueing: &b" + this.queue.getTotalPlayerCount(),
                "",
                "&f&lDaily Streak: &bN/A",
                " &f1. &bNULL &f- &bN/A",
                " &f2. &bNULL &f- &bN/A",
                " &f3. &bNULL &f- &bN/A",
                "",
                "&aClick to play!"
        );

        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        if (this.plugin.getServerService().isQueueingEnabled(player)) return;

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(this.queue.getKit().getName()).getElo() : 0);
        this.playNeutral(player);

        PlayerUtil.reset(player, false);
        player.closeInventory();
    }
}