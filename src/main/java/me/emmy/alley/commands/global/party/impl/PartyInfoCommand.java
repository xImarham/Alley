package me.emmy.alley.commands.global.party.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.party.Party;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 24/05/2024 - 19:17
 */

public class PartyInfoCommand extends BaseCommand {
    @Override
    @Command(name = "party.info")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (Alley.getInstance().getPartyRepository().getPartyLeader(playerUUID) == null) {
            player.sendMessage(CC.translate("&cYou are not in any party."));
            return;
        }

        String members = Alley.getInstance().getPartyRepository().getPartyLeader(playerUUID).getMembers().stream()
                .map(uuid -> Alley.getInstance().getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .map(Player::getName)
                .collect(Collectors.joining(", "));

        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate("&d&lParty Info"));
        player.sendMessage(CC.translate(" &fLeader: &d" + player.getName()));

        if (members.isEmpty()) {
            player.sendMessage(CC.translate(" &fMembers: &cNo members."));
        } else {
            player.sendMessage(CC.translate(" &fMembers: &d" + members));
        }

        player.sendMessage(CC.FLOWER_BAR);
        player.sendMessage(CC.translate(""));
    }
}
