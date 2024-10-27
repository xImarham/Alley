package me.emmy.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.pagination.ItemBuilder;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.impl.StandAloneArena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.chat.CC;
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

        for (Arena arena : Alley.getInstance().getArenaRepository().getArenas()) {
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
        private Arena arena;

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
            Alley.getInstance().getDuelRepository().sendDuelRequest(player, targetPlayer, kit, arena);
            player.sendMessage(CC.translate("&aYou have sent a duel request to " + targetPlayer.getName() + " in the " + arena.getName() + " arena with the " + kit.getName() + " kit."));
        }
    }
}