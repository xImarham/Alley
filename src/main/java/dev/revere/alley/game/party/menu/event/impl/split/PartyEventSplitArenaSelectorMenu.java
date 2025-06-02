package dev.revere.alley.game.party.menu.event.impl.split;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2024 - 09:37
 */
@AllArgsConstructor
public class PartyEventSplitArenaSelectorMenu extends Menu {
    private Kit kit;

    @Override
    public String getTitle(Player player) {
        return "&b&lSelect an arena";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (AbstractArena arena : Alley.getInstance().getArenaService().getArenas()) {
            if (arena.getKits().contains(kit.getName()) && arena.isEnabled() &&
                    (!(arena instanceof StandAloneArena) || !((StandAloneArena) arena).isActive())) {
                buttons.put(buttons.size(), new PartyEventSplitArenaSelectorButton(kit, arena));
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class PartyEventSplitArenaSelectorButton extends Button {
        private Kit kit;
        private AbstractArena arena;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER).name("&b" + arena.getName()).durability(0).hideMeta()
                    .lore(Collections.singletonList(
                            "&7Click to select this arena."
                    ))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Party party = Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getParty();
            if (party == null) {
                player.closeInventory();
                player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
                return;
            }

            Player playerA = party.getLeader();
            Player playerB = Bukkit.getPlayer(party.getMembers().get(1));

            MatchGamePlayerImpl gamePlayerA = new MatchGamePlayerImpl(playerA.getUniqueId(), playerA.getName());
            MatchGamePlayerImpl gamePlayerB = new MatchGamePlayerImpl(playerB.getUniqueId(), playerB.getName());

            GameParticipant<MatchGamePlayerImpl> participantA = new TeamGameParticipant<>(gamePlayerA);
            GameParticipant<MatchGamePlayerImpl> participantB = new TeamGameParticipant<>(gamePlayerB);

            List<Player> players = party.getMembers().stream().map(Bukkit::getPlayer).collect(Collectors.toList());
            Collections.shuffle(players);

            for (Player player1 : players) {
                if (player1.equals(playerA) || player1.equals(playerB)) continue;
                MatchGamePlayerImpl gamePlayer = new MatchGamePlayerImpl(player1.getUniqueId(), player1.getName());

                if (players.indexOf(player1) % 2 == 0) {
                    participantA.getPlayers().add(gamePlayer);
                } else {
                    participantB.getPlayers().add(gamePlayer);
                }
            }

            Alley.getInstance().getMatchRepository().createAndStartMatch(
                    this.kit, this.arena, participantA, participantB, true, false
            );
        }
    }
}