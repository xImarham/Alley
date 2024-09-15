package me.emmy.alley.profile.cosmetic.command.impl.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmetic;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmeticRepository;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticSetCommand extends BaseCommand {
    @Command(name = "cosmetic.set", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&cUsage: /cosmetic set <player> <type> <cosmetic>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());

        String type = args[1];
        String cosmeticName = args[2];

        Map<String, ICosmeticRepository<?>> cosmeticRepositories = Alley.getInstance().getCosmeticRepository().getCosmeticRepositories();
        ICosmeticRepository<?> repository = cosmeticRepositories.get(type);
        if (repository == null) {
            player.sendMessage(CC.translate("&cInvalid cosmetic type"));
            return;
        }

        ICosmetic cosmetic = repository.getCosmetics().stream()
                .filter(c -> c.getName().equalsIgnoreCase(cosmeticName))
                .findFirst().orElse(null);

        if (cosmetic == null) {
            player.sendMessage(CC.translate("&cInvalid cosmetic name"));
            return;
        }

        profile.getProfileData().getProfileCosmeticData().setActiveCosmetic(type, cosmetic);
        player.sendMessage(CC.translate("&aSuccessfully set &b" + cosmetic.getName() + " &aas the active cosmetic for &b" + target.getName()));
    }
}