package dev.revere.alley.command.impl.other;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 18/07/2025
 */
public class MusicCommand extends BaseCommand {
    @CommandData(name = "music", aliases = "playmusic", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.playEffect(player.getLocation(), Effect.RECORD_PLAY, 2257);
    }
}
