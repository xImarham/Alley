package dev.revere.alley.game.party.menu.event.impl.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.menu.event.impl.PartyEventSplitArenaSelectorMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.kit.getIcon()).name("&b&l" + this.kit.getName()).durability(this.kit.getDurability()).hideMeta()
                .lore(
                        "&7" + this.kit.getDescription(),
                        "",
                        "&aClick to select this kit."
                )
                .hideMeta().build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (player.hasPermission("alley.party.arena.selector")) {
            new PartyEventSplitArenaSelectorMenu(this.kit).openMenu(player);
            return;
        }

        Party party = this.plugin.getProfileService().getProfile(player.getUniqueId()).getParty();
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

        this.plugin.getMatchService().createAndStartMatch(
                this.kit, this.plugin.getArenaService().getRandomArena(this.kit), participantA, participantB, true, false, false
        );
    }
}