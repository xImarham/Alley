package me.emmy.alley.arena.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaToggleCommand extends BaseCommand {
    @Command(name = "arena.toggle", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena toggle <arenaName>"));
            return;
        }

        String arenaName = args[0];
        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getMinimum() == null || Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getMaximum() == null || Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getPos1() == null || Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getPos2() == null) {
            player.sendMessage(CC.translate("&cYou must finish configuring this arena before enabling or disabling!"));
            return;
        }

        if (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).getKits().isEmpty()) {
            player.sendMessage(CC.translate("&cYou must add at least one kit to this arena before enabling or disabling!"));
            return;
        }

        Alley.getInstance().getArenaRepository().getArenaByName(arenaName).setEnabled(!Alley.getInstance().getArenaRepository().getArenaByName(arenaName).isEnabled());
        Alley.getInstance().getArenaRepository().saveArena(Alley.getInstance().getArenaRepository().getArenaByName(arenaName));
        player.sendMessage(CC.translate("&aArena &b" + arenaName + "&a has been " + (Alley.getInstance().getArenaRepository().getArenaByName(arenaName).isEnabled() ? "enabled" : "disabled") + "&a!"));
    }
}
