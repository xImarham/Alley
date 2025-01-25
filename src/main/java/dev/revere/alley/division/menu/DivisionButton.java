package dev.revere.alley.division.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.division.Division;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.data.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 25/01/2025
 */
@Getter
@AllArgsConstructor
public class DivisionButton extends Button {
    private final Division division;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.division.getIcon())
                .durability(this.division.getDurability())
                .name("&b&l" + this.division.getDisplayName() + " Division")
                .lore(
                        "&f&l● &bTiers: &f" + this.division.getTiers().size(),
                        "  &7▶ (" + this.division.getTiers().get(0).getRequiredWins() + " - " + this.division.getTotalWins() + " Wins)",
                        "",
                        " &fFor each kit, you'll have",
                        " &fa division based on your",
                        " &bUnranked &fwins.",
                        "",
                        "&aClick to see your titles."
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.sendMessage(CC.translate("&cThis feature is not implemented yet."));
        player.closeInventory();

        //open titles menu like on minemen - still need to implement that =)
    }
}