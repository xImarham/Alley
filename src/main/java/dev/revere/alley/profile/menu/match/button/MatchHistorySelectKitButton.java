package dev.revere.alley.profile.menu.match.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.match.MatchHistoryViewMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
@AllArgsConstructor
public class MatchHistorySelectKitButton extends Button {
    protected final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId());

        List<AbstractMatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        int count = (int) matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()))
                .count();

        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .lore(
                        "&fMatches stored for this kit: &6" + count,
                        "",
                        "&aClick to view!"
                )
                .durability(this.kit.getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new MatchHistoryViewMenu(this.kit).openMenu(player);
    }
}
