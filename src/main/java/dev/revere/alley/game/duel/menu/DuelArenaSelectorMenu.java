package dev.revere.alley.game.duel.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 22:13
 */
@AllArgsConstructor
public class DuelArenaSelectorMenu extends Menu {
    private Player targetPlayer;
    private Kit kit;

    @Override
    public String getTitle(Player player) {
        return "Select an arena.";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 0;

        for (AbstractArena arena : Alley.getInstance().getArenaService().getArenas()) {
            if (arena.getKits().contains(kit.getName()) && arena.isEnabled()) {
                buttons.put(slot++, new DuelArenaSelectorButton(targetPlayer, kit, arena));
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class DuelArenaSelectorButton extends Button {
        private Player targetPlayer;
        private Kit kit;
        private AbstractArena arena;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER).name("&6" + arena.getName()).durability(0).hideMeta()
                    .lore(
                            "",
                            "&7Click to send a duel request to " + targetPlayer.getName() + " in the " + arena.getName() + " arena.",
                            ""
                    )
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();

            Alley.getInstance().getDuelRequestService().createAndSendRequest(player, targetPlayer, kit, arena);
        }
    }
}