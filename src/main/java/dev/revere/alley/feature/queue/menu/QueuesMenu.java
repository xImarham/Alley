package dev.revere.alley.feature.queue.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueRepository;
import dev.revere.alley.feature.queue.enums.EnumQueueType;
import dev.revere.alley.feature.queue.menu.button.UnrankedButton;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.ffa.menu.FFAButton;
import dev.revere.alley.game.ffa.menu.FFAMenu;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.data.item.ItemBuilder;
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
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class QueuesMenu extends Menu {
    private final Alley plugin;
    private final String menuType;

    public QueuesMenu() {
        this.plugin = Alley.getInstance();
        this.menuType = this.plugin.getConfigService().getMenusConfig().getString("queues-menu.type", "DEFAULT");
    }

    /**
     * Get the title of the menu.
     *
     * @param player the player to get the title for
     * @return the title of the menu
     */
    @Override
    public String getTitle(Player player) {
        if (this.isModern()) {
            Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());
            return "&b&l" + profile.getQueueType().getMenuTitle();
        }
        return "&b&lSolo Unranked Queues";
    }

    /**
     * Get the buttons for the menu.
     *
     * @param player the player to get the buttons for
     * @return the buttons for the menu
     */
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        QueueRepository queueRepository = this.plugin.getQueueRepository();
        Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());

        if (this.isDefault()) {
            this.getUnrankedButton(11, buttons, queueRepository, profile);
            this.getBotsButton(13, buttons, queueRepository, profile);
            this.getFFAButton(15, buttons, queueRepository, profile);

            this.addGlass(buttons, (byte) 15);

        } else if (this.isModern()) {
            this.getUnrankedButton(2, buttons, queueRepository, profile);
            this.getBotsButton(4, buttons, queueRepository, profile);
            this.getFFAButton(6, buttons, queueRepository, profile);

            switch (profile.getQueueType()) {
                case UNRANKED:
                    for (Queue queue : this.plugin.getQueueRepository().getQueues()) {
                        if (!queue.isRanked()) {
                            buttons.put(queue.getKit().getUnrankedslot(), new UnrankedButton(queue));
                        }
                    }

                    break;
                case BOTS:
                    //TODO: Implement bot fights :v
                    break;
                case FFA:
                    int slot = 10;

                    for (AbstractFFAMatch match : this.plugin.getFfaRepository().getMatches()) {
                        buttons.put(slot, new FFAButton(match));
                        slot++;
                    }

                    break;
            }

            this.addGlass(buttons, (byte) 15);

        }

        return buttons;
    }

    /**
     * Check if the menu type is modern.
     *
     * @return true if the menu type is modern
     */
    private boolean isModern() {
        return this.menuType.equalsIgnoreCase("MODERN");
    }

    /**
     * Check if the menu type is default.
     *
     * @return true if the menu type is default
     */
    private boolean isDefault() {
        return this.menuType.equalsIgnoreCase("DEFAULT");
    }

    /**
     * Get the properties of the unranked button.
     *
     * @param slot the slot of the button
     * @param buttons the buttons map
     * @param queueRepository the queue repository
     * @param profile the player's profile
     */
    private void getUnrankedButton(int slot, Map<Integer, Button> buttons, QueueRepository queueRepository, Profile profile) {
        String selected = profile.getQueueType() == EnumQueueType.UNRANKED ? "&a&lSELECTED" : "&aClick to play!";
        buttons.put(slot, new QueuesButton("&b&lSolos", Material.DIAMOND_SWORD, 0, Arrays.asList(
                "&7Casual 1v1s with",
                "&7no loss penalty.",
                "",
                "&bPlayers: &f" + queueRepository.getPlayerCountOfGameType("Unranked"),
                "",
                this.isModern() ? selected : "&aClick to play!"
        )));
    }

    /**
     * Get the properties of the bots button.
     *
     * @param slot the slot of the button
     * @param buttons the buttons map
     * @param queueRepository the queue repository
     * @param profile the player's profile
     */
    private void getBotsButton(int slot, Map<Integer, Button> buttons, QueueRepository queueRepository, Profile profile) {
        String selected = profile.getQueueType() == EnumQueueType.BOTS ? "&a&lSELECTED" : "&c&lIN DEVELOPMENT";
        buttons.put(slot, new QueuesButton("&b&lBots", Material.GOLD_SWORD, 0, Arrays.asList(
                "&7Practice against bots",
                "&7to improve your skills.",
                "",
                "&bPlayers: &f" + queueRepository.getPlayerCountOfGameType("Bots"),
                "",
                this.isModern() ? selected : "&aClick to play!"
        )));
    }

    /**
     * Get the properties of the FFA button.
     *
     * @param slot the slot of the button
     * @param buttons the buttons map
     * @param queueRepository the queue repository
     * @param profile the player's profile
     */
    private void getFFAButton(int slot, Map<Integer, Button> buttons, QueueRepository queueRepository, Profile profile) {
        String selected = profile.getQueueType() == EnumQueueType.FFA ? "&a&lSELECTED" : "&aClick to play!";
        buttons.put(slot, new QueuesButton("&b&lFFA", Material.GOLD_AXE, 0, Arrays.asList(
                "&7Free for all with",
                "&7infinity respawns.",
                "",
                "&bPlayers: &f" + queueRepository.getPlayerCountOfGameType("FFA"),
                "",
                this.isModern() ? selected : "&aClick to play!"
        )));
    }

    @Override
    public int getSize() {
        if (this.isModern()) {
            return 9 *5;
        } else if (this.isDefault()) {
            return 9 * 3;
        } else {
            return 9 * 3;
        }
    }

    @AllArgsConstructor
    public class QueuesButton extends Button {
        private String displayName;
        private Material material;
        private int durability;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.material)
                    .name(this.displayName)
                    .durability(this.durability)
                    .lore(this.lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType != ClickType.LEFT) return;

            if (isModern()) {
                Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
                switch (this.material) {
                    case DIAMOND_SWORD:
                        profile.setQueueType(EnumQueueType.UNRANKED);
                        break;
                    case GOLD_AXE:
                        profile.setQueueType(EnumQueueType.FFA);
                        break;
                }

                new QueuesMenu().openMenu(player);

            } else if (isDefault()) {
                switch (this.material) {
                    case DIAMOND_SWORD:
                        new UnrankedMenu().openMenu(player);
                        break;
                    case GOLD_AXE:
                        new FFAMenu().openMenu(player);
                        break;
                    case GOLD_SWORD:
                        player.sendMessage(CC.translate("&cThis feature is currently in development."));
                        break;
                }
            }

            playNeutral(player);
        }
    }
}