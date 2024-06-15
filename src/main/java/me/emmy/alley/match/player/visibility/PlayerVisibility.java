package me.emmy.alley.match.player.visibility;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.match.player.GamePlayer;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 13/06/2024 - 23:24
 * Credit: Joeleoli
 */
@Getter
public class PlayerVisibility {

    public void handle(Player viewer) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            handle(viewer, target);
        }
    }

    public void handle(Player viewer, Player target) {
        if (viewer == null || target == null) {
            return;
        }

        Profile viewerProfile = Alley.getInstance().getProfileRepository().getProfile(viewer.getUniqueId());
        Profile targetProfile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());

        if (viewerProfile.getState() == EnumProfileState.LOBBY || viewerProfile.getState() == EnumProfileState.WAITING) {
            if (viewer.equals(target)) {
                return;
            }

            if (viewerProfile.getParty() != null && viewerProfile.getParty().getMembers().contains(target.getUniqueId())) {
                viewer.showPlayer(target);
            } else {
                viewer.hidePlayer(target);
            }

        } else if (viewerProfile.getState() == EnumProfileState.PLAYING) {
            if (viewer.equals(target)) {
                return;
            }

            GamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

            if (targetGamePlayer != null) {
                if (!targetGamePlayer.isDead()) {
                    viewer.showPlayer(target);
                } else {
                    viewer.hidePlayer(target);
                }
            } else {
                viewer.hidePlayer(target);
            }
            if (viewerProfile.getMatch().getMatchKit().isSettingEnabled("KitSettingsShowHealthImpl")) {
                Objective objective = viewer.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

                if (objective == null) {
                    objective = viewer.getScoreboard().registerNewObjective("showhealth", "health");
                }
                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("❤"));
                objective.getScore(target.getName()).setScore((int) Math.floor(target.getHealth() / 2));
            }
        } else if (viewerProfile.getState() == EnumProfileState.PLAYING_EVENT) {
            if (targetProfile.getState() == EnumProfileState.PLAYING_EVENT) {
                viewer.showPlayer(target);
            } else {
                viewer.hidePlayer(target);
            }
        } else if (viewerProfile.getState() == EnumProfileState.SPECTATING) {
            GamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

            if (targetGamePlayer != null) {
                if (!targetGamePlayer.isDead() && !targetGamePlayer.isDisconnected()) {
                    viewer.showPlayer(target);
                } else {
                    viewer.hidePlayer(target);
                }
            } else {
                viewer.hidePlayer(target);
            }
        }
    }
}
