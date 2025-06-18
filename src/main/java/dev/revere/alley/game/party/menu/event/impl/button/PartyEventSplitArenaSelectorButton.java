package dev.revere.alley.game.party.menu.event.impl.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
public class PartyEventSplitArenaSelectorButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final Kit kit;
    private final AbstractArena arena;

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

        Party party = this.plugin.getProfileService().getProfile(player.getUniqueId()).getParty();
        if (party == null) {
            player.closeInventory();
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        List<Player> allPartyPlayers = party.getMembers().stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());

        Collections.shuffle(allPartyPlayers);

        Player leaderA = allPartyPlayers.get(0);
        Player leaderB = allPartyPlayers.get(1);

        MatchGamePlayerImpl gameLeaderA = new MatchGamePlayerImpl(leaderA.getUniqueId(), leaderA.getName());
        MatchGamePlayerImpl gameLeaderB = new MatchGamePlayerImpl(leaderB.getUniqueId(), leaderB.getName());

        GameParticipant<MatchGamePlayerImpl> participantA = new TeamGameParticipant<>(gameLeaderA);
        GameParticipant<MatchGamePlayerImpl> participantB = new TeamGameParticipant<>(gameLeaderB);

        int totalPlayers = allPartyPlayers.size();
        int teamATargetSize = totalPlayers / 2;
        int currentTeamACount = 1;

        for (int i = 2; i < allPartyPlayers.size(); i++) {
            Player currentPlayer = allPartyPlayers.get(i);
            MatchGamePlayerImpl gamePlayer = new MatchGamePlayerImpl(currentPlayer.getUniqueId(), currentPlayer.getName());

            if (currentTeamACount < teamATargetSize) {
                participantA.getPlayers().add(gamePlayer);
                currentTeamACount++;
            } else {
                participantB.getPlayers().add(gamePlayer);
            }
        }

        this.plugin.getMatchService().createAndStartMatch(
                this.kit, this.plugin.getArenaService().getRandomArena(this.kit), participantA, participantB, true, false, false
        );
    }
}