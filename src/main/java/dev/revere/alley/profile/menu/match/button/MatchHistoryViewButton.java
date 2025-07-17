package dev.revere.alley.profile.menu.match.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.game.match.data.impl.MatchDataSoloImpl;
import dev.revere.alley.tool.date.DateFormatter;
import dev.revere.alley.tool.date.enums.EnumDateFormat;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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

            UUID winnerUUID = matchDataSolo.getWinner();
            UUID loserUUID = matchDataSolo.getLoser();

            String winnerName = Bukkit.getOfflinePlayer(winnerUUID).getName();
            String loserName = Bukkit.getOfflinePlayer(loserUUID).getName();

            DateFormatter dateFormatter = new DateFormatter(EnumDateFormat.DATE_PLUS_TIME, matchDataSolo.getCreationTime());
            String date = dateFormatter.getDateFormat().format(dateFormatter.getDate());

            String rankedStatus = matchDataSolo.isRanked() ? "&6Ranked" : "&9Unranked";

            Kit kit = Alley.getInstance().getService(IKitService.class).getKit(matchDataSolo.getKit());
            AbstractArena arena = Alley.getInstance().getService(IArenaService.class).getArenaByName(matchDataSolo.getArena());

            return new ItemBuilder(Material.SKULL_ITEM)
                    .name("&a&l" + winnerName + " &7vs &c&l" + loserName + " &7(" + rankedStatus + "&7)")
                    .setSkull(winnerName)
                    .lore(
                            "&7" + date,
                            "",
                            "&6&lParticipants",
                            " &f● Winner: &a" + winnerName,
                            " &f● Loser: &c" + loserName,
                            "",
                            "&6&lMatch Details",
                            " &f● Kit: &6" + kit.getDisplayName(),
                            " &f● Arena: &6" + arena.getDisplayName(),
                            "",
                            "&aClick to view more details!"
                    )
                    .hideMeta()
                    .durability(3)
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

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.sendMessage(CC.translate("&cThis is not yet implemented."));
        player.closeInventory();
    }
}