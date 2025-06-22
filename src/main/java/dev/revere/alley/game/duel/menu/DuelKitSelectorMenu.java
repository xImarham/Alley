package dev.revere.alley.game.duel.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRankedImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:11
 */
@AllArgsConstructor
public class DuelKitSelectorMenu extends Menu {
    protected final Alley plugin = Alley.getInstance();
    private final Player targetPlayer;

    @Override
    public String getTitle(Player player) {
        return "Duel " + this.targetPlayer.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.plugin.getKitService().getKits().stream()
                .filter(Kit::isEnabled)
                .filter(kit -> kit.isSettingEnabled(KitSettingRankedImpl.class))
                .forEach(kit -> buttons.put(buttons.size(), new DuelButton(this.targetPlayer, kit)));

        return buttons;
    }

    @AllArgsConstructor
    private static class DuelButton extends Button {
        protected final Alley plugin = Alley.getInstance();
        private Player targetPlayer;
        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.kit.getIcon()).name(this.kit.getDisplayName() == null ? "&b&l" + this.kit.getName() : this.kit.getDisplayName()).durability(this.kit.getDurability()).hideMeta()
                    .lore(
                            "&7" + this.kit.getDescription(),
                            "",
                            "&aClick to send a duel request to " + this.targetPlayer.getName() + "!"
                    )
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (player.hasPermission("alley.duel.arena.selector")) {
                new DuelArenaSelectorMenu(this.targetPlayer, this.kit).openMenu(player);
                return;
            }

            player.closeInventory();

            Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

            this.plugin.getDuelRequestService().sendDuelRequest(player, this.targetPlayer, this.kit, profile.getParty() != null);
            player.sendMessage(CC.translate("&aYou have sent a duel request to " + this.targetPlayer.getName() + " in the " + this.plugin.getDuelRequestService().getDuelRequest(player, this.targetPlayer).getArena().getName() + " arena with the " + this.kit.getName() + " kit."));
        }
    }
}