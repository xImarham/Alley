package dev.revere.alley.profile.shop.command.admin;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class SetCoinsCommand extends BaseCommand {
    @Command(name = "coins.set", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            player.sendMessage(CC.translate("&cUsage: /coins set <player> <amount>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());

        try {
            int amount = Integer.parseInt(args[1]);
            profile.getProfileData().setCoins(amount);
            player.sendMessage(CC.translate("&aSuccessfully set " + target.getName() + "'s coins to " + amount + "."));
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
        }
    }
}
