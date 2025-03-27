package dev.revere.alley.feature.queue.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class UnrankedButton extends Button {
    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = queue.getKit();
        return new ItemBuilder(kit.getIcon()).name(kit.getDisplayName()).durability(kit.getIconData()).hideMeta().lore(Arrays.asList(
                "&7" + kit.getDescription(),
                "",
                "&fIn Queue: &b" + queue.getProfiles().size(),
                "&fIn Fights: &b" + queue.getQueueFightCount(),
                "",
                "&fTotal Wins: &b" + Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getProfileData().getUnrankedKitData().get(kit.getName()).getWins(),
                "&fTotal Losses: &b" + Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getProfileData().getUnrankedKitData().get(kit.getName()).getLosses(),
                "",
                "&fClick to join the &b" + kit.getName() + " &fqueue!")
        ).hideMeta().build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        if (Alley.getInstance().getServerService().check(player)) return;

        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());
        queue.addPlayer(player, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player, false);
        player.closeInventory();
        playNeutral(player);
        Alley.getInstance().getHotbarService().applyHotbarItems(player, HotbarType.QUEUE);
    }
}