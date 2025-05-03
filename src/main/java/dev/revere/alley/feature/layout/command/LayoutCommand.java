package dev.revere.alley.feature.layout.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
public class LayoutCommand extends BaseCommand {
    @CommandData(name = "layout", aliases = {"layouteditor", "kiteditor"}, description = "Edit the layout of a kit.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getProfileService().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou are not in the lobby!"));
            return;
        }

        this.plugin.getLayoutService().getLayoutMenu().openMenu(player);
    }
}
