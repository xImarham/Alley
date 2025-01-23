package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.Alley;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 24/05/2024 - 19:17
 */
public class PartyInfoCommand extends BaseCommand {
    @Override
    @Command(name = "party.info", aliases = {"p.info"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
        Party party = partyHandler.getPartyByMember(player.getUniqueId());

        if (party == null) {
            player.sendMessage(CC.translate(Locale.NOT_IN_PARTY.getMessage()));
            return;
        }

        UUID leaderUUID = party.getLeader().getUniqueId();

        String members = party.getMembers().stream()
                .filter(uuid -> !uuid.equals(leaderUUID))
                .map(uuid -> Alley.getInstance().getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .map(Player::getName)
                .collect(Collectors.joining(", "));

        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("messages.yml");
        List<String> info = config.getStringList("party.info-command.text");
        String noMembersFormat = CC.translate(config.getString("party.info-command.no-members-format"));

        for (String line : info) {
            player.sendMessage(CC.translate(line)
                    .replace("{leader}", Alley.getInstance().getServer().getPlayer(leaderUUID).getName())
                    .replace("{members}", members.isEmpty() ? noMembersFormat : members));
        }
    }
}
