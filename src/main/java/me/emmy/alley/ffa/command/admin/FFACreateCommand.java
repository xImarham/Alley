package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFACreateCommand extends BaseCommand {
    @Command(name = "ffa.create", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&cUsage: /ffa create <arena> <kit> <maxPlayers>"));
            return;
        }

        String arenaName = args[0];
        Arena arena = Alley.getInstance().getArenaRepository().getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate("&cArena not found."));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(CC.translate("&cThis arena is not a FFA arena."));
            return;
        }

        String kitName = args[1];
        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        int maxPlayers = Integer.parseInt(args[2]);

        if (Alley.getInstance().getFfaRepository().getFFAMatch(kitName) != null) {
            player.sendMessage(CC.translate("&cThere is already a FFA match with the name " + kitName + "."));
            return;
        }

        Alley.getInstance().getFfaRepository().createFFAMatch(arena, kit, maxPlayers);
        player.sendMessage(CC.translate("&aSuccessfully created the FFA match."));
        Alley.getInstance().getProfileRepository().loadProfiles();
        player.sendMessage(CC.translate("&7Additionally, all profiles have been reloaded."));
    }
}
