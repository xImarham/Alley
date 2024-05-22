package me.emmy.alley.commands.global.party.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.locale.ConfigLocale;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 22/05/2024 - 20:36
 */

public class PartyLeaveCommand extends BaseCommand {
    @Override
    @Command(name = "party.leave")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        /*
         * TODO: check if player is leader of the party. If so, disband the party.
         *  Else if player is just in a party, make them leave it.
         */

        /* if (player == party leader) {
         * player.sendMessage(CC.translate(ConfigLocale.PARTY_DISBANDED.getMessage()));
         * -- DISBAND PARTY --
         * return
         * }
         */

        Alley.getInstance().getHotbarUtility().applySpawnItems(player);
        player.sendMessage(CC.translate(ConfigLocale.PARTY_LEFT.getMessage()));
    }
}
