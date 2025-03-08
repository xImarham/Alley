package dev.revere.alley.feature.spawn.command;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:45
 */
public class SpawnItemsCommand extends BaseCommand {
    @Override
    @CommandData(name = "spawnitems", aliases = {"lobbyitems"}, permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        player.sendMessage(CC.translate("&aYou were given the spawn items!"));
    }
}