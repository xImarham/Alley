package dev.revere.alley.feature.leaderboard.menu.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.tool.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 3/3/2025
 */
public class LeaderboardKitButton extends Button {
    private final Kit kit;
    private final List<LeaderboardPlayerData> leaderboard;
    private final EnumLeaderboardType type;

    public LeaderboardKitButton(Kit kit, List<LeaderboardPlayerData> leaderboard, EnumLeaderboardType type) {
        this.kit = kit;
        this.leaderboard = leaderboard;
        this.type = type;
    }

    @Override
    public ItemStack getButtonItem(Player player) {

        switch (this.type) {
            case RANKED:
                return new ItemBuilder(this.kit.getIcon())
                        .name(this.kit.getDisplayName())
                        .durability(this.kit.getDurability())
                        .hideMeta()
                        .lore(this.leaderboard.stream()
                                .limit(10)
                                .map(data -> {
                                    int currentRank = this.leaderboard.indexOf(data) + 1;

                                    String rankNumber;
                                    switch (currentRank) {
                                        case 1:
                                            rankNumber = "&6&l✫1";
                                            break;
                                        case 2:
                                            rankNumber = "&7&l✫2";
                                            break;
                                        case 3:
                                            rankNumber = "&8&l✫3";
                                            break;
                                        default:
                                            rankNumber = "&b" + currentRank + ".";
                                            break;
                                    }

                                    return rankNumber + " &f" + data.getName() + " &b- " + data.getElo();
                                })
                                .collect(Collectors.toList()))
                        .build();
            default:
                return this.inDevelopment();
        }
    }

    /**
     * To be used when the leaderboard is in development.
     *
     * @return an ItemStack with a barrier icon and a message stating that the leaderboard is in development
     */
    private ItemStack inDevelopment() {
        return new ItemBuilder(Material.BARRIER)
                .name("&c&lComing Soon")
                .lore(
                        "&7This leaderboard is currently",
                        "&7being worked on. Please check",
                        "&7back later."
                )
                .build();
    }

}
