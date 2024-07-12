package me.emmy.alley.host.impl.tournament;

import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.utils.chat.CC;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 17:08
 */
public class TournamentLogger {

    private static Tournament getTournament() {
        return Alley.getInstance().getTournament();
    }

    public static void broadcastPlayerJoin(Player joinedPlayer) {
        String message = CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.player.joined")
                .replace("{player}", joinedPlayer.getName())
                .replace("{players}", String.valueOf(getTournament().getPlayers().size()))
                .replace("{maxPlayers}", String.valueOf(getTournament().getMaxPlayers()))
        );
        getTournament().notifyPlayers(message);
    }

    public static void broadcastPlayerLeave(Player leftPlayer) {
        String message = CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.player.left")
                .replace("{player}", leftPlayer.getName())
                .replace("{players}", String.valueOf(getTournament().getPlayers().size()))
                .replace("{maxPlayers}", String.valueOf(getTournament().getMaxPlayers()))
        );
        getTournament().notifyPlayers(message);
    }

    public static void broadcastWaiting() {
        Tournament tournament = getTournament();
        List<String> list = ConfigHandler.getInstance().getMessagesConfig().getStringList("tournament-broadcast.waiting.message");
        List<BaseComponent> messages = new ArrayList<>();

        for (String message : list) {
            if (message.equals("[clickable]")) {
                messages.add(getTextComponent());
            } else {
                String formattedMessage = message
                        .replace("{host}", tournament.getHost().getName())
                        .replace("{kit}", tournament.getKit().getName())
                        .replace("{players}", String.valueOf(tournament.getPlayers().size()))
                        .replace("{maxPlayers}", String.valueOf(tournament.getMaxPlayers()))
                        .replace("{remaining}", String.valueOf(tournament.getMaxPlayers() - tournament.getPlayers().size()));
               messages.add(new TextComponent(CC.translate(formattedMessage)));
            }
        }

        for (BaseComponent message : messages) {
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(message));
        }
    }

    private static @NotNull TextComponent getTextComponent() {
        TextComponent clickableJoinMessage = new TextComponent(CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.waiting.clickable-format")));
        clickableJoinMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tournament join"));
        String hover = ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.waiting.clickable-hover");
        BaseComponent[] hoverComponent = new ComponentBuilder(CC.translate(hover)).create();
        clickableJoinMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        return clickableJoinMessage;
    }

    public static void broadcastStarting() {
        List<String> messages = Arrays.asList(
                "",
                "&d&lTOURNAMENT",
                " &f• &dHost: &f" + getTournament().getHost().getName(),
                " &f• &dKit: &f" + getTournament().getKit().getName(),
                " &f• &dPlayers: &f" + getTournament().getPlayers().size() + "/" + getTournament().getMaxPlayers(),
                "",
                "&dTournament is starting in " + getTournament().getCountdown() + " seconds!",
                ""
        );
        messages.forEach(message -> Bukkit.broadcastMessage(CC.translate(message)));
    }

    public static void broadcastStartingRound() {
        List<String> messages = Arrays.asList(
                "",
                "&d&lTOURNAMENT",
                " &f• &dHost: &f" + getTournament().getHost().getName(),
                " &f• &dKit: &f" + getTournament().getKit().getName(),
                "",
                "&dRound " + getTournament().getRound() + " is starting...",
                ""
        );
        messages.forEach(message -> Bukkit.broadcastMessage(CC.translate(message)));
    }

    public static void broadcastSpectating() {
        List<String> messages = Arrays.asList(
                "",
                "&d&lTOURNAMENT",
                " &f• &dYou are now spectating the tournament",
                "",
                " &f• &d" + getTournament().getPlayerTeams().get(0).getPlayerNames(),
                " &bvs",
                " &d• &d" + getTournament().getPlayerTeams().get(1).getPlayerNames(),
                ""
        );
        messages.forEach(message -> Bukkit.broadcastMessage(CC.translate(message)));
    }
}
