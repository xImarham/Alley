package me.emmy.alley.match.command;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class SpectateCommand extends BaseCommand {
    @Command(name = "spectate", aliases = {"spec", "watch"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /spectate <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        if (profile.getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou can only spectate players in the lobby."));
            return;
        }

        Profile targetProfile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        if (targetProfile.getState() != EnumProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou are unable to spectate that player."));
            return;
        }

        targetProfile.getMatch().addSpectator(player, target);
    }
}
