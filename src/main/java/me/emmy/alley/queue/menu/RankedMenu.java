package me.emmy.alley.queue.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.util.PlayerUtil;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.button.BackButton;
import me.emmy.alley.api.menu.pagination.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
public class RankedMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lSolo Ranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        //buttons.put(0, new BackButton(new QueuesMenu()));

        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (queue.isRanked()) {
                buttons.put(queue.getKit().getRankedslot(), new RankedButton(queue));
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
    public static class RankedButton extends Button {

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
                    "&fTotal Wins: &b" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getKitData().get(kit.getName()).getWins(),
                    "&fTotal Losses: &b" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getKitData().get(kit.getName()).getLosses(),
                    "&fElo: &b" + Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getKitData().get(kit.getName()).getElo(),
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
            queue.addPlayer(player, queue.isRanked() ? profile.getProfileData().getKitData().get(queue.getKit().getName()).getElo() : 0);
            PlayerUtil.reset(player);
            player.closeInventory();
            playNeutral(player);
            Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.QUEUE);
        }
    }
}

