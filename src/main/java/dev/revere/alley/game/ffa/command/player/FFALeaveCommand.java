package dev.revere.alley.game.ffa.command.player;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.combat.CombatRepository;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFALeaveCommand extends BaseCommand {
    @Command(name = "ffa.leave", aliases = "leaveffa")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getFfaMatch() == null) {
            player.sendMessage(CC.translate("&cYou are not in a FFA match."));
            return;
        }

        CombatRepository combatRepository = Alley.getInstance().getCombatRepository();
        if (combatRepository.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, Bukkit.getPlayer(combatRepository.getCombat(player.getUniqueId()).getAttacker()));
        }

        profile.getFfaMatch().leave(player);
    }
}
