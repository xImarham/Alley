package me.emmy.alley.host.impl.tournament;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.util.chat.CC;
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
@UtilityClass
public class TournamentLogger {
    private TournamentRepository getTournament() {
        return Alley.getInstance().getTournamentRepository();
    }

    /**
     * broadcast of a player joining the tournament
     *
     * @param player the player
     */
    public void broadcastPlayerJoin(Player player) {
        String message = CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.player.joined")
                .replace("{player}", player.getName())
                .replace("{players}", String.valueOf(getTournament().getPlayers().size()))
                .replace("{maxPlayers}", String.valueOf(getTournament().getMaxPlayers()))
        );
        getTournament().notifyPlayers(message);
    }

    /**
     * broadcast of a player leaving the tournament
     *
     * @param player the player
     */
    public void broadcastPlayerLeave(Player player) {
        String message = CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.player.left")
                .replace("{player}", player.getName())
                .replace("{players}", String.valueOf(getTournament().getPlayers().size()))
                .replace("{maxPlayers}", String.valueOf(getTournament().getMaxPlayers()))
        );
        getTournament().notifyPlayers(message);
    }

    public void broadcastWaiting() {
        TournamentRepository tournamentRepository = getTournament();
        List<String> list = ConfigHandler.getInstance().getMessagesConfig().getStringList("tournament-broadcast.waiting.message");
        List<BaseComponent> messages = new ArrayList<>();

        for (String message : list) {
            if (message.equals("[clickable]")) {
                messages.add(getTextComponent());
            } else {
                String formattedMessage = message
                        .replace("{host}", tournamentRepository.getHost().getName())
                        .replace("{kit}", tournamentRepository.getKit().getName())
                        .replace("{players}", String.valueOf(tournamentRepository.getPlayers().size()))
                        .replace("{maxPlayers}", String.valueOf(tournamentRepository.getMaxPlayers()))
                        .replace("{remaining}", String.valueOf(tournamentRepository.getMaxPlayers() - tournamentRepository.getPlayers().size()));
               messages.add(new TextComponent(CC.translate(formattedMessage)));
            }
        }

        for (BaseComponent message : messages) {
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(message));
        }
    }

    private @NotNull TextComponent getTextComponent() {
        TextComponent clickableJoinMessage = new TextComponent(CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.waiting.clickable-format")));
        clickableJoinMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tournament join"));
        String hover = ConfigHandler.getInstance().getMessagesConfig().getString("tournament-broadcast.waiting.clickable-hover");
        BaseComponent[] hoverComponent = new ComponentBuilder(CC.translate(hover)).create();
        clickableJoinMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        return clickableJoinMessage;
    }

    public void broadcastStarting() {
        List<String> messages = Arrays.asList(
                "",
                "&b&lTOURNAMENT",
                " &f• &bHost: &f" + getTournament().getHost().getName(),
                " &f• &bKit: &f" + getTournament().getKit().getName(),
                " &f• &bPlayers: &f" + getTournament().getPlayers().size() + "/" + getTournament().getMaxPlayers(),
                "",
                "&bTournament is starting in " + getTournament().getCountdown() + " seconds!",
                ""
        );
        messages.forEach(message -> Bukkit.broadcastMessage(CC.translate(message)));
    }

    public void broadcastStartingRound() {
        List<String> messages = Arrays.asList(
                "",
                "&b&lTOURNAMENT",
                " &f• &bHost: &f" + getTournament().getHost().getName(),
                " &f• &bKit: &f" + getTournament().getKit().getName(),
                "",
                "&bRound " + getTournament().getRound() + " is starting...",
                ""
        );
        messages.forEach(message -> Bukkit.broadcastMessage(CC.translate(message)));
    }

    public void broadcastSpectating() {
        List<String> messages = Arrays.asList(
                "",
                "&b&lTOURNAMENT",
                " &f• &bYou are now spectating the tournament",
                "",
                " &f• &b" + getTournament().getPlayerTeams().get(0).getPlayerNames(),
                " &bvs",
                " &b• &b" + getTournament().getPlayerTeams().get(1).getPlayerNames(),
                ""
        );
        messages.forEach(message -> Bukkit.broadcastMessage(CC.translate(message)));
    }
}