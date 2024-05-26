package me.emmy.alley.party.command.impl.member;

import me.emmy.alley.Alley;
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
    @Command(name = "party.info", aliases = {"p.info"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Alley.getInstance().getPartyRepository().getPartyByLeader(player) == null) {
            player.sendMessage(CC.translate("&cYou are not in any party."));
            return;
        }

        String members = Alley.getInstance().getPartyRepository().getPartyByLeader(player).getMembers().stream()
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
