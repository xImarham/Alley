package me.emmy.alley.queue.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.ffa.menu.FFAMenu;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */

@AllArgsConstructor
public class QueuesMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Select a Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new QueuesButton("&bUnranked Queue", Material.IRON_SWORD, (short) 0, Arrays.asList(
                "&7Casual 1v1s with",
                "&7no loss penalty.",
                "",
                "&bPlayers: &f" + Alley.getInstance().getPlayerCountOfGameType("Unranked"),
                "",
                "&aClick to select a kit!"
        )));

        buttons.put(12, new QueuesButton("&bRanked Queue", Material.DIAMOND_SWORD, (short) 0, Arrays.asList(
                "&7Competitive 1v1s with",
                "&7elo and leaderboards.",
                "",
                "&bPlayers: &f" + Alley.getInstance().getPlayerCountOfGameType("Ranked"),
                "",
                "&aClick to select a kit!"
        )));

        buttons.put(14, new QueuesButton("&bBot Queue", Material.SKULL_ITEM, (short) 3, Arrays.asList(
                "&7Practice against bots",
                "&7to improve your skills.",
                "",
                "&bPlayers: &f" + Alley.getInstance().getPlayerCountOfGameType("Bots"),
                "",
                "&cCurrently in development."
        )));

        buttons.put(16, new QueuesButton("&bFFA Arena", Material.GOLD_AXE, (short) 0, Arrays.asList(
                "&7Free for all with",
                "&7infinity respawns.",
                "",
                "&bPlayers: &f" + Alley.getInstance().getPlayerCountOfGameType("FFA"),
                "",
                "&aClick to select a kit!"
        )));

        addGlass(buttons, (byte) 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @AllArgsConstructor
    public static class QueuesButton extends Button {

        private String displayName;
        private Material material;
        private short data;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(material)
                    .name(displayName)
                    .durability(data)
                    .lore(lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                return;
            }

            switch (material) {
                case IRON_SWORD:
                    new UnrankedMenu().openMenu(player);
                    break;
                case DIAMOND_SWORD:
                    new RankedMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new FFAMenu().openMenu(player);
                    break;
                case SKULL_ITEM:
                    //new BotMenu().openMenu(player);
                    player.sendMessage(CC.translate(ErrorMessage.DEBUG_STILL_IN_DEVELOPMENT));
                    break;
            }

            playNeutral(player);
        }
    }
}
