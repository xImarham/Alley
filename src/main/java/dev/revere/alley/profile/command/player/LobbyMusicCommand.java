package dev.revere.alley.profile.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.menu.music.MusicDiscSelectorMenu;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class LobbyMusicCommand extends BaseCommand {
    @CommandData(
            name = "lobbymusic",
            aliases = {"music"},
            permission = "alley.donator.command.lobbymusic"
    )
    @Override
    public void onCommand(CommandArgs command) {
        new MusicDiscSelectorMenu().openMenu(command.getPlayer());
    }
}