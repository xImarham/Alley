package me.emmy.alley.game.duel.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.pagination.PaginatedMenu;
import me.emmy.alley.game.duel.DuelRequest;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.item.ItemBuilder;
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
        return "&b&lYour Duel Requests";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

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
            return new ItemBuilder(Material.PAPER)
                    .name("&fRequest from " + duelRequest.getSender().getName())
                    .lore(Arrays.asList(
                            "&fArena: &f" + duelRequest.getArena().getDisplayName(),
                            "&fKit: &f" + duelRequest.getKit().getDisplayName(),
                            " &7" + duelRequest.getKit().getDescription(),
                            "",
                            "&fExpires in: &b" + duelRequest.getRemainingTimeFormatted(),
                            "",
                            "&aClick to accept!"
                            ))
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (duelRequest.hasExpired()) {
                player.sendMessage(CC.translate("&cThis duel request has expired."));
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
}