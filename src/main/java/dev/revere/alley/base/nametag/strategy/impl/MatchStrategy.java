package dev.revere.alley.base.nametag.strategy.impl;

import dev.revere.alley.base.kit.setting.impl.mode.KitSettingHideAndSeekImpl;
import dev.revere.alley.base.nametag.NametagContext;
import dev.revere.alley.base.nametag.NametagView;
import dev.revere.alley.base.nametag.NametagVisibility;
import dev.revere.alley.base.nametag.strategy.NametagStrategy;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.impl.MatchHideAndSeekImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class MatchStrategy implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        if (context.getViewerProfile().getState() != EnumProfileState.PLAYING) {
            return null;
        }

        AbstractMatch match = context.getViewerProfile().getMatch();
        if (match == null) {
            return null;
        }

        if (context.getTargetProfile().getMatch() == null || !context.getTargetProfile().getMatch().equals(match)) {
            return null;
        }

        if (match.getKit().isSettingEnabled(KitSettingHideAndSeekImpl.class)) {
            MatchHideAndSeekImpl hideAndSeekMatch = (MatchHideAndSeekImpl) match;
            GameParticipant<?> seekers = hideAndSeekMatch.getParticipantA();

            boolean viewerIsSeeker = seekers.containsPlayer(context.getViewer().getUniqueId());
            boolean targetIsSeeker = seekers.containsPlayer(context.getTarget().getUniqueId());

            if (viewerIsSeeker) {
                if (targetIsSeeker) {
                    return new NametagView(CC.translate("&a"), "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(CC.translate("&c"), "", NametagVisibility.NEVER);
                }
            } else {
                if (targetIsSeeker) {
                    return new NametagView(CC.translate("&c"), "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(CC.translate("&a"), "", NametagVisibility.ALWAYS);
                }
            }
        }

        if (!match.isTeamMatch()) {
            return null;
        }

        if (match.isInSameTeam(context.getViewer(), context.getTarget())) {
            return new NametagView(CC.translate(ChatColor.GREEN.toString()), "");
        } else {
            return new NametagView(CC.translate(ChatColor.RED.toString()), "");
        }
    }
}