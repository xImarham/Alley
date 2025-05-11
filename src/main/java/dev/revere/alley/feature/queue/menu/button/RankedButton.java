package dev.revere.alley.feature.queue.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.hotbar.enums.EnumHotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class RankedButton extends Button {

    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = queue.getKit();
        return new ItemBuilder(kit.getIcon())
                .name(kit.getDisplayName())
                .durability(kit.getDurability())
                .hideMeta()
                .lore(this.getLore(kit, player))
                .hideMeta().build();
    }

    /**
     * Get the lore for the kit.
     *
     * @param kit the kit to get the lore for
     * @return the lore for the kit
     */
    private @NotNull List<String> getLore(Kit kit, Player player) {
        List<String> lore = new ArrayList<>();
        if (!kit.getDescription().isEmpty()) {
            Collections.addAll(lore,
                    "&7" + kit.getDescription(),
                    ""
            );
        }

        Collections.addAll(lore,
                "&fPlaying: &b" + this.queue.getQueueFightCount(),
                "&fQueueing: &b" + this.queue.getProfiles().size(),
                "",
                "&f&lYour ELO: &b" + Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getProfileData().getRankedKitData().get(kit.getName()).getElo(),
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

        if (Alley.getInstance().getServerService().isQueueingEnabled(player)) return;

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        if (profile.getProfileData().isRankedBanned()) {
            player.closeInventory();
            Arrays.asList(
                    "",
                    "&c&lRANKED BAN",
                    "&cYou are currently banned from ranked queues.",
                    "&7You may appeal at &b&ndiscord.gg/alley-practice&7.",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
            return;
        }

        queue.addPlayer(player, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player, false);
        player.closeInventory();
        playNeutral(player);
        Alley.getInstance().getHotbarService().applyHotbarItems(player, EnumHotbarType.QUEUE);
    }
}
