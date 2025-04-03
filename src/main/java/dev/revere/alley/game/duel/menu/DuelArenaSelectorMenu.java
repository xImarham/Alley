package dev.revere.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
            if (arena.getKits().contains(kit.getName()) && arena.isEnabled() &&
                    (!(arena instanceof StandAloneArena) || !((StandAloneArena) arena).isActive())) {
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
            return new ItemBuilder(Material.PAPER).name("&b" + arena.getName()).durability(0).hideMeta()
                    .lore(Arrays.asList(
                            "",
                            "&7Click to send a duel request to " + targetPlayer.getName() + " in the " + arena.getName() + " arena.",
                            "")
                    )
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();
            Alley.getInstance().getDuelRequestService().sendDuelRequest(player, targetPlayer, kit, arena);
            player.sendMessage(CC.translate("&aYou have sent a duel request to " + targetPlayer.getName() + " in the " + arena.getName() + " arena with the " + kit.getName() + " kit."));
        }
    }
}