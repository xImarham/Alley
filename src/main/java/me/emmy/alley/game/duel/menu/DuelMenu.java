package me.emmy.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.game.duel.DuelRequest;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.util.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:11
 */
@AllArgsConstructor
public class DuelMenu extends Menu {

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
                    .lore(Collections.singletonList("&7Click to duel " + player.getName() + " with this kit."))
                    .durability(kit.getIconData())
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Alley.getInstance().getDuelRepository().sendDuelRequest(player, targetPlayer, kit);
        }
    }
}
