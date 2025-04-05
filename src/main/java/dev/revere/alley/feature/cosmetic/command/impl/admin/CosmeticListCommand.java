package dev.revere.alley.feature.cosmetic.command.impl.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticListCommand extends BaseCommand {
    @CommandData(name = "cosmetic.list", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Map<String, ICosmeticRepository<?>> repositories = this.plugin.getCosmeticRepository().getCosmeticRepositories();

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
