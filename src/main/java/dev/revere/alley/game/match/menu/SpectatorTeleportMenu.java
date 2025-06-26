package dev.revere.alley.game.match.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.statistic.StatisticsMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.reflection.utility.ReflectionUtility;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
@AllArgsConstructor
public class SpectatorTeleportMenu extends PaginatedMenu {
    private final FileConfiguration config = Alley.getInstance().getConfigService().getMenusConfig();
    private final String path = "menus.spectator-teleport";

    private final AbstractMatch match;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return this.config.getString(this.path + ".title", "&cERROR");

    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, this.config.getInt(this.path + ".buttons.match-info-button.glass-durability", 15));
        buttons.put(this.config.getInt(this.path + ".buttons.match-info-button.slot", 4), new MatchInfoButton(this.match));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<GameParticipant<MatchGamePlayerImpl>> participants = this.match.getParticipants();

        participants.forEach(participant ->
                participant.getPlayers().forEach(gamePlayer ->
                        buttons.put(buttons.size(), new SpectatorTeleportButton(gamePlayer))
                )
        );

        return buttons;
    }

    @AllArgsConstructor
    private static class SpectatorTeleportButton extends Button {
        private final FileConfiguration config = Alley.getInstance().getConfigService().getMenusConfig();
        private final String path = "menus.spectator-teleport";

        private final MatchGamePlayerImpl gamePlayer;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Alley.getInstance().getProfileService().getProfile(this.gamePlayer.getUuid());
            MatchRegularImpl match = (MatchRegularImpl) profile.getMatch();

            List<String> lore = new ArrayList<>();
            List<String> configLore = this.config.getStringList(this.path + ".buttons.spectator-teleport-button.lore");
            if (configLore.isEmpty()) {
                configLore = Collections.singletonList(
                        "&cSomething went wrong with your config."
                );
            }

            int ping = ReflectionUtility.getPing(this.gamePlayer.getPlayer());
            ChatColor team = match.getTeamColor(match.getParticipant(this.gamePlayer.getPlayer()));
            String teamColor = team.name();
            int elo = match.isRanked() ? profile.getProfileData().getRankedKitData().get(match.getKit().getName()).getElo() : -1;

            for (String line : configLore) {
                lore.add(line
                        .replace("{player-color}", String.valueOf(profile.getNameColor()))
                        .replace("{username}", this.gamePlayer.getUsername())
                        .replace("{ping}", String.valueOf(ping))
                        .replace("{team}", team + teamColor)
                        .replace("{elo}", String.valueOf(elo))
                );
            }

            String name = this.config.getString(this.path + ".buttons.spectator-teleport-button.name", "&6{username}")
                    .replace("{username}", this.gamePlayer.getUsername())
                    .replace("{player-color}", String.valueOf(profile.getNameColor())
                    );

            return new ItemBuilder(Material.SKULL_ITEM)
                    .setSkull(this.gamePlayer.getUsername())
                    .name(name)
                    .lore(lore)
                    .durability(3)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                if (this.gamePlayer.isDead() || this.gamePlayer.isDisconnected()) {
                    player.sendMessage(CC.translate("&cThis player is either dead or disconnected."));
                    return;
                }
                player.teleport(this.gamePlayer.getPlayer().getLocation());
            } else if (clickType == ClickType.RIGHT) {
                new StatisticsMenu(this.gamePlayer.getPlayer()).openMenu(player);
            }
        }
    }

    @AllArgsConstructor
    private static class MatchInfoButton extends Button {
        private final AbstractMatch match;

        @Override
        public ItemStack getButtonItem(Player player) {
            int playerParticipantSize = this.match.getParticipants().stream()
                    .mapToInt(participant -> participant.getPlayers().size())
                    .sum();

            FileConfiguration config = Alley.getInstance().getConfigService().getMenusConfig();
            String path = "menus.spectator-teleport.buttons.match-info-button";
            String name = config.getString(path + ".name", "&6&lMatch Info");
            List<String> lore = config.getStringList(path + ".lore");
            if (lore.isEmpty()) {
                lore = Collections.singletonList(
                        "&cSomething went wrong with your config."
                );
            }

            List<String> replacedLore = new ArrayList<>();
            for (String line : lore) {
                replacedLore.add(line
                        .replace("{kit}", this.match.getKit().getName())
                        .replace("{arena}", this.match.getArena().getName())
                        .replace("{players}", String.valueOf(playerParticipantSize))
                );
            }

            return new ItemBuilder(Material.PAPER)
                    .name(name)
                    .lore(replacedLore)
                    .hideMeta()
                    .build();
        }
    }
}