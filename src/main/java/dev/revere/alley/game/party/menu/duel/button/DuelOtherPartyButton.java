package dev.revere.alley.game.party.menu.duel.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class DuelOtherPartyButton extends Button {
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