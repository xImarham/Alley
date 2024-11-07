package dev.revere.alley.game.duel.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.game.duel.menu.DuelKitSelectorMenu;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:09
 */
public class DuelCommand extends BaseCommand {
    @Command(name = "duel")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/duel &b<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (target == player) {
            player.sendMessage(CC.translate("&cYou cannot duel yourself."));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to duel a player."));
            return;
        }

        new DuelKitSelectorMenu(target).openMenu(player);
    }
}