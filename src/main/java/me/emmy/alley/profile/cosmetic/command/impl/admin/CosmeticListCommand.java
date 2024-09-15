package me.emmy.alley.profile.cosmetic.command.impl.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmeticRepository;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticListCommand extends BaseCommand {
    @Command(name = "cosmetic.list", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Map<String, ICosmeticRepository<?>> repositories = Alley.getInstance().getCosmeticRepository().getCosmeticRepositories();

        player.sendMessage("");

        if (repositories.isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Cosmetics available."));
        }

        repositories.forEach((name, repository) -> {
            int size = repository.getCosmetics().size();
            if (size != 0) {
                player.sendMessage(CC.translate("     &b&l" + name + " &f(" + size + "&f)"));
                repository.getCosmetics().forEach(cosmetic -> player.sendMessage(CC.translate("      &f● &b" + cosmetic.getName())));
            }
        });

        player.sendMessage("");
    }
}
