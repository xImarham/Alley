package dev.revere.alley.game.party.menu.duel;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 21:01
 */
public class DuelOtherPartyMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&b&lDuel other parties";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Alley.getInstance().getPartyService().getParties().stream()
                .sorted(Comparator.comparing(party -> party.getLeader().getName()))
                //.filter(party -> !party.getLeader().equals(player))
                .sorted(Comparator.comparingInt(party -> party.getMembers().size()))
                .forEach(party -> buttons.put(buttons.size(), new DuelOtherPartyButton(party)))
        ;

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @AllArgsConstructor
    private static class DuelOtherPartyButton extends Button {

        private final Party party;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = getLore();

            ItemStack itemStack = new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3))
                    .name("&b&l" + party.getLeader().getName() + "'s Party")
                    .lore(lore)
                    .build();
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwner(Bukkit.getPlayer(party.getLeader().getName()).getName());
            itemStack.setItemMeta(meta);

            return itemStack;
        }

        /**
         * Get the lore of the item.
         *
         * @return the lore of the item
         */
        private @NotNull List<String> getLore() {
            List<String> lore = new ArrayList<>();
            lore.add(" &bMembers: &f(" + party.getMembers().size() + ")");
            for (UUID member : party.getMembers()) {
                lore.add("  &f● &b" + Bukkit.getPlayer(member).getName());
            }
            lore.add("");
            lore.add("&aClick to duel this party.");
            return lore;
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (party.getLeader().equals(player)) {
                player.sendMessage(CC.translate("&cYou can't duel your own party."));
                return;
            }

            player.closeInventory();
            player.sendMessage(CC.translate("&cemmy was lazy to finish this class"));
        }
    }
}