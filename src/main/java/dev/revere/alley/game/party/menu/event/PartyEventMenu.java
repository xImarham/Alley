package dev.revere.alley.game.party.menu.event;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.menu.event.impl.ffa.PartyEventFFAMenu;
import dev.revere.alley.game.party.menu.event.impl.split.PartyEventSplitMenu;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:29
 */
@AllArgsConstructor
public class PartyEventMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lChoose a party event type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new PartyEventButton(
                Material.DIAMOND_SWORD, 0,
                "&b&lTeam split",
                Arrays.asList(
                        "&fSplit the party into",
                        "&f2 teams and fight",
                        "&fagainst each other.",
                        "",
                        "&aClick to select a kit."
                )
        ));

        buttons.put(13, new PartyEventButton(
                Material.GOLD_AXE, 0,
                "&b&lFree for all",
                Arrays.asList(
                        "&fEvery player fights",
                        "&fagainst each other.",
                        "",
                        "&aClick to select a kit."
                )
        ));

        buttons.put(15, new PartyEventButton(
                Material.REDSTONE, 0,
                "&cBest of 3 Sumo",
                Arrays.asList(
                        "&fThis event is not",
                        "&fimplemented yet.",
                        "",
                        "&c&mClick to start the event."
                )
        ));

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class PartyEventButton extends Button {
        private Material material;
        private int durability;
        private String name;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(material)
                    .name(name)
                    .lore(lore)
                    .durability(durability)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Party party = Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getParty();
            if (party == null) {
                player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
                return;
            }

            if (party.getLeader() != player) {
                player.sendMessage(CC.translate(PartyLocale.NOT_LEADER.getMessage()));
                return;
            }

            if (party.getMembers().size() < 2) {
                player.sendMessage(CC.translate("&cYou need at least 2 players in your party to start an event."));
                return;
            }

            switch (material) {
                case DIAMOND_SWORD:
                    new PartyEventSplitMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new PartyEventFFAMenu().openMenu(player);
                    break;
                case INK_SACK:
                    // Start best of 3 sumo event
                    break;
            }
        }
    }
}