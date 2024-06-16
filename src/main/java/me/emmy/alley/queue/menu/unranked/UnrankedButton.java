package me.emmy.alley.queue.menu.unranked;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.pagination.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class UnrankedButton extends Button {

    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = queue.getKit();
        return new ItemBuilder(kit.getIcon()).name(kit.getDisplayName()).durability(kit.getIconData()).hideMeta().lore(Arrays.asList(
                "&7" + kit.getDescription(),
                "",
                "&fIn Queue: &d" + queue.getProfiles().size(),
                "&fIn Fights: &d" + queue.getQueueFightCount(),
                "",
                "&fTotal Wins: &d" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getKitData().get(kit.getName()).getWins(),
                "&fTotal Losses: &d" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getKitData().get(kit.getName()).getLosses(),
                "",
                "&fClick to join the &d" + kit.getName() + " &fqueue!")
        ).hideMeta().build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        queue.addPlayer(player, queue.isRanked() ? profile.getProfileData().getKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player);
        player.closeInventory();
        playNeutral(player);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.QUEUE);
    }
}