package me.emmy.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:11
 */
@AllArgsConstructor
public class DuelKitSelectorMenu extends Menu {
    private final Player targetPlayer;

    @Override
    public String getTitle(Player player) {
        return "Duel " + targetPlayer.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Alley.getInstance().getKitRepository().getKits().stream()
                .filter(kit -> kit.isSettingEnabled(KitSettingRankedImpl.class))
                .forEach(kit -> buttons.put(buttons.size(), new DuelButton(targetPlayer, kit)));

        return buttons;
    }

    @AllArgsConstructor
    private static class DuelButton extends Button {
        private Player targetPlayer;
        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(kit.getIcon())
                    .name("&b" + kit.getName())
                    .lore(Arrays.asList(
                            kit.getDescription(),
                            "",
                            "&7Click to send a duel request to " + targetPlayer.getName() + ".",
                            ""))
                    .durability(kit.getIconData())
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (player.hasPermission("alley.duel.arena.selector")) {
                new DuelArenaSelectorMenu(targetPlayer, kit).openMenu(player);
                return;
            }

            Alley.getInstance().getDuelRepository().sendDuelRequest(player, targetPlayer, kit);
            player.sendMessage(CC.translate("&aYou have sent a duel request to " + targetPlayer.getName() + " in the " + Alley.getInstance().getDuelRepository().getDuelRequest(player, targetPlayer).getArena().getName() + " arena with the " + kit.getName() + " kit."));
            player.closeInventory();
        }
    }
}