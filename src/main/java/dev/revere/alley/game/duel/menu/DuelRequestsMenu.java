package dev.revere.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.game.duel.DuelRequest;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.item.ItemBuilder;
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
 * @date 22/10/2024 - 18:18
 */
@AllArgsConstructor
public class DuelRequestsMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&b&lDuel Requests";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        addGlassHeader(buttons, 15);

        buttons.put(4, new RefreshDuelRequestsButton());

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Alley.getInstance().getDuelRepository().getDuelRequests()
                .stream()
                .filter(duelRequest -> !duelRequest.getSender().equals(player))
                .forEach(duelRequest -> buttons.put(buttons.size(), new DuelRequestsButton(duelRequest)));


        return buttons;
    }

    @AllArgsConstructor
    private static class DuelRequestsButton extends Button {
        private DuelRequest duelRequest;
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER).name("&b&l" + duelRequest.getSender().getName()).durability(0).hideMeta()
                    .lore(Arrays.asList(
                            "&fKit: &f" + duelRequest.getKit().getDisplayName(),
                            "&fArena: &f" + duelRequest.getArena().getDisplayName(),
                            "",
                            "&fExpires in: &b" + duelRequest.getRemainingTimeFormatted(),
                            "",
                            "&aClick to accept!"
                    ))
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (duelRequest.hasExpired()) {
                player.sendMessage(CC.translate("&cThis duel request has expired."));
                new DuelRequestsMenu().openMenu(player);
                return;
            }

            if (duelRequest.getArena() == null) {
                player.sendMessage(CC.translate("&cThis duel request has no setup arena."));
                new DuelRequestsMenu().openMenu(player);
                return;
            }

            if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getMatch() != null) {
                player.sendMessage(CC.translate("&cYou are already in a match."));
                return;
            }

            Alley.getInstance().getDuelRepository().acceptPendingRequest(duelRequest);
            player.closeInventory();
        }
    }

    @AllArgsConstructor
    private static class RefreshDuelRequestsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD)
                    .name("&a&lRefresh")
                    .lore("&aClick to refresh the duel requests.")
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            new DuelRequestsMenu().openMenu(player);
        }
    }
}