package me.emmy.alley.spawn.command;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 29/04/2024 - 18:45
 */
public class SpawnItemsCommand extends BaseCommand {
    @Override
    @Command(name = "spawnitems", aliases = {"lobbyitems"}, permission = "alley.admin")
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        player.sendMessage(CC.translate("&aYou were given the spawn items!"));
    }
}