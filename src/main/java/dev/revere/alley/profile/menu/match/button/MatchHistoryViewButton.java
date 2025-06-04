package dev.revere.alley.profile.menu.match.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.game.match.data.impl.MatchDataSoloImpl;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
@AllArgsConstructor
public class MatchHistoryViewButton extends Button {
    protected final AbstractMatchData matchData;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.matchData instanceof MatchDataSoloImpl) {
            MatchDataSoloImpl matchDataSolo = (MatchDataSoloImpl) this.matchData;

            UUID playerUUID = player.getUniqueId();

            UUID opponentUUID;
            if (matchDataSolo.getWinner().equals(playerUUID)) {
                opponentUUID = matchDataSolo.getLoser();
            } else {
                opponentUUID = playerUUID;
            }

            String winnerName = Bukkit.getOfflinePlayer(matchDataSolo.getWinner()).getName();
            String loserName = Bukkit.getOfflinePlayer(opponentUUID).getName();

            return new ItemBuilder(Material.SKULL_ITEM)
                    .name("&b&l" + winnerName + " &7vs &c&l" + loserName)
                    .lore(
                            "&fWinner: &a" + winnerName + " &7(You)",
                            "&fLoser: &c" + loserName,
                            "",
                            "&fKit: &b" + matchDataSolo.getKit(),
                            "&fArena: &b" + matchDataSolo.getArena()
                    )
                    .setSkull(loserName)
                    .hideMeta()
                    .build();
        } else {
            return new ItemBuilder(Material.BARRIER)
                    .name("&c&lNot implemented")
                    .lore(
                            "&fThis is not implemented for team matches yet."
                    )
                    .hideMeta()
                    .build();
        }
    }
}
