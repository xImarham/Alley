package me.emmy.alley.profile.cosmetic.command.impl.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.impl.ProfileCosmeticData;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticGetSelectedCommand extends BaseCommand {
    @Command(name = "cosmetic.getselected", aliases = {"cosmetic.get"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /cosmetic getselected <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        player.sendMessage(CC.translate("     &b&lSelected Cosmetics for " + target.getName()));

        ProfileCosmeticData cosmeticData = profile.getProfileData().getProfileCosmeticData();
        String killEffect = cosmeticData.getSelectedKillEffect();
        String soundEffect = cosmeticData.getSelectedSoundEffect();

        player.sendMessage(CC.translate("      &f● &bKill Effect: &f" + killEffect));
        player.sendMessage(CC.translate("      &f● &bSound Effect: &f" + soundEffect));
    }
}