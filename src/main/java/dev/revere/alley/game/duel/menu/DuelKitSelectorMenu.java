package dev.revere.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.item.ItemBuilder;
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
                .filter(Kit::isEnabled)
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
            return new ItemBuilder(kit.getIcon()).name(kit.getDisplayName() == null ? "&b&l" + kit.getName() : kit.getDisplayName()).durability(kit.getIconData()).hideMeta()
                    .lore(Arrays.asList(
                            "&7" + kit.getDescription(),
                            "",
                            "&aClick to send a duel request to " + targetPlayer.getName() + "!"
                    ))
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (player.hasPermission("alley.duel.arena.selector")) {
                new DuelArenaSelectorMenu(targetPlayer, kit).openMenu(player);
                return;
            }

            player.closeInventory();
            Alley.getInstance().getDuelRepository().sendDuelRequest(player, targetPlayer, kit);
            player.sendMessage(CC.translate("&aYou have sent a duel request to " + targetPlayer.getName() + " in the " + Alley.getInstance().getDuelRepository().getDuelRequest(player, targetPlayer).getArena().getName() + " arena with the " + kit.getName() + " kit."));
        }
    }
}