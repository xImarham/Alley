package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.party.Party;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
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

        PartyRepository partyRepository = Alley.getInstance().getPartyRepository();
        Party party = partyRepository.getPartyByMember(player.getUniqueId());

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

        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("messages.yml");
        List<String> info = config.getStringList("party.info-command.text");
        String noMembersFormat = CC.translate(config.getString("party.info-command.no-members-format"));

        for (String line : info) {
            player.sendMessage(CC.translate(line)
                    .replace("{leader}", Alley.getInstance().getServer().getPlayer(leaderUUID).getName())
                    .replace("{members}", members.isEmpty() ? noMembersFormat : members));
        }
    }
}
