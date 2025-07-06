package dev.revere.alley.base.arena.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.artificial.IAIArenaGeneratorService;
import dev.revere.alley.base.arena.artificial.IArenaBuildService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @author Remi
 * @project alley-practice
 * @date 6/07/2025
 */
public class ArenaGenerateCommand extends BaseCommand {
    @CommandData(name = "arena.generate", isAdminOnly = true, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&cUsage: /arena generate <name> <type> <description>"));
            player.sendMessage(CC.translate("&cExample: /arena generate mystic_castle standalone A mystical castle arena with towers and bridges"));
            return;
        }

        String arenaName = args[0];
        String arenaTypeStr = args[1];

        EnumArenaType arenaType;
        try {
            arenaType = EnumArenaType.valueOf(arenaTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid arena type. Use: STANDALONE or SHARED"));
            return;
        }

        StringBuilder description = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            description.append(args[i]).append(" ");
        }
        String userPrompt = description.toString().trim();
        String dimensions = "200x80x200 blocks (Length x Height x Width)";

        player.sendMessage(CC.translate("&a&lGenerating Arena..."));
        player.sendMessage(CC.translate("&7Name: &f" + arenaName));
        player.sendMessage(CC.translate("&7Type: &f" + arenaType));
        player.sendMessage(CC.translate("&7Description: &f" + userPrompt));
        player.sendMessage(CC.translate("&7Dimensions: &f" + dimensions));
        player.sendMessage(CC.translate("&e&lPlease wait while the AI generates your arena..."));

        IAIArenaGeneratorService generator = Alley.getInstance().getService(IAIArenaGeneratorService.class);
        IArenaBuildService builder = Alley.getInstance().getService(IArenaBuildService.class);

        player.sendMessage(CC.translate("&6[AI] &eSending your request to the AI architect... This may take a moment."));
        CompletableFuture<String> scriptFuture = generator.generateBuildScript(userPrompt, dimensions);

        scriptFuture.thenCompose(script -> {
            player.sendMessage(CC.translate("&a&lAI generation complete! Building arena..."));
            return builder.buildArenaFromScript(player, arenaName, arenaType, script);
        }).thenAccept(arena -> {
            player.sendMessage(CC.translate("&a&l✓ Arena Generated Successfully!"));
            player.sendMessage(CC.translate("&7Arena Name: &f" + arena.getName()));
            player.sendMessage(CC.translate("&7Arena Type: &f" + arenaType));
            player.sendMessage(CC.translate("&7Center Location: &f" + arena.getCenter().getBlockX() + ", " +
                    arena.getCenter().getBlockY() + ", " + arena.getCenter().getBlockZ()));
            player.sendMessage(CC.translate("&7Dimensions: &f" +
                    (arena.getMaximum().getBlockX() - arena.getMinimum().getBlockX()) + "x" +
                    (arena.getMaximum().getBlockY() - arena.getMinimum().getBlockY()) + "x" +
                    (arena.getMaximum().getBlockZ() - arena.getMinimum().getBlockZ())));
            player.sendMessage(CC.translate("&aThe arena is now ready for use!"));
        }).exceptionally(throwable -> {
            player.sendMessage(CC.translate("&c&lArena Generation Failed!"));
            player.sendMessage(CC.translate("&cError: " + throwable.getMessage()));
            player.sendMessage(CC.translate("&7Please check the console for detailed error information."));
            return null;
        });
    }
}
