package dev.revere.alley.game.ffa.command.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFACreateCommand extends BaseCommand {
    @CommandData(name = "ffa.create", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&cUsage: /ffa create <arena> <kit> <maxPlayers>"));
            return;
        }

        String arenaName = args[0];
        AbstractArena arena = this.plugin.getArenaService().getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate("&cArena not found."));
            return;
        }

        if (arena.getType() != EnumArenaType.FFA) {
            player.sendMessage(CC.translate("&cThis arena is not a FFA arena."));
            return;
        }

        String kitName = args[1];
        Kit kit = this.plugin.getKitService().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        int maxPlayers = Integer.parseInt(args[2]);

        if (this.plugin.getFfaService().getFFAMatch(kitName) != null) {
            player.sendMessage(CC.translate("&cThere is already a FFA match with the name " + kitName + "."));
            return;
        }

        this.plugin.getFfaService().createFFAMatch(arena, kit, maxPlayers);
        player.sendMessage(CC.translate("&aSuccessfully created the FFA match."));
        this.plugin.getProfileService().loadProfiles();
        player.sendMessage(CC.translate("&7Additionally, all profiles have been reloaded."));
    }
}
