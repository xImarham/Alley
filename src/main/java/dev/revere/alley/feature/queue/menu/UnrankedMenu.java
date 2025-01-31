package dev.revere.alley.feature.queue.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.util.data.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 10:28
 */
public class UnrankedMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lUnranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenu()));

        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (!queue.isRanked()) {
                buttons.put(queue.getKit().getUnrankedslot(), new UnrankedButton(queue));
            }
        }

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @AllArgsConstructor
    public static class UnrankedButton extends Button {

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
                    "&fTotal Wins: &b" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getUnrankedKitData().get(kit.getName()).getWins(),
                    "&fTotal Losses: &b" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getUnrankedKitData().get(kit.getName()).getLosses(),
                    "",
                    "&fClick to join the &b" + kit.getName() + " &fqueue!")
            ).hideMeta().build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                return;
            }

            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            queue.addPlayer(player, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
            PlayerUtil.reset(player, false);
            player.closeInventory();
            playNeutral(player);
            Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.QUEUE);
        }
    }
}